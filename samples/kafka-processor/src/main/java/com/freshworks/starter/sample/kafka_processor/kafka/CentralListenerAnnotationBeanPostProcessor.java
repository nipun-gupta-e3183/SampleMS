package com.freshworks.starter.sample.kafka_processor.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.*;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.*;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CentralListenerAnnotationBeanPostProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware {
    private final Logger logger = LoggerFactory.getLogger(CentralListenerAnnotationBeanPostProcessor.class);
    private final ListenerScope listenerScope = new ListenerScope();
    private BeanExpressionContext expressionContext;
    private BeanFactory beanFactory;
    private BeanExpressionResolver resolver;
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
    private MethodCentralListenerEndpointRegistry centralListenerEndpointRegistry;
    private final DefaultFormattingConversionService defaultFormattingConversionService =
            new DefaultFormattingConversionService();
    private Charset charset = StandardCharsets.UTF_8;
    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    @Autowired
    public void setCentralListenerEndpointRegistry(MethodCentralListenerEndpointRegistry centralListenerEndpointRegistry) {
        this.centralListenerEndpointRegistry = centralListenerEndpointRegistry;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory,
                    this.listenerScope);
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, CentralListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass, this::findListenerAnnotations);
        if (annotatedMethods.isEmpty()) {
            this.nonAnnotatedClasses.add(bean.getClass());
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No @KafkaListener annotations found on bean type: " + bean.getClass());
            }
        }
        else {
            // Non-empty set of methods
            for (Map.Entry<Method, CentralListener> entry : annotatedMethods.entrySet()) {
                Method method = entry.getKey();
                processCentralListener(entry.getValue(), method, bean);
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(annotatedMethods.size() + " @KafkaListener methods processed on bean '"
                        + beanName + "': " + annotatedMethods);
            }
        }
        return bean;
    }

    protected void processCentralListener(CentralListener centralListener, Method method, Object bean) {
        Method methodToUse = checkProxy(method, bean);
        MethodCentralListenerEndpoint endpoint = new MethodCentralListenerEndpoint();
        processListener(endpoint, centralListener, bean, methodToUse);
    }

    private void processListener(MethodCentralListenerEndpoint endpoint, CentralListener centralListener, Object bean,
                                 Method methodToUse) {
        String beanRef = centralListener.beanRef();
        if (StringUtils.hasText(beanRef)) {
            this.listenerScope.addListener(beanRef, bean);
        }
        endpoint.setBean(bean);
        endpoint.setMethod(methodToUse);
        if (centralListener.messageFilterEnabled()) {
            Method messageFilterMethod = getMessageFilterMethod(centralListener, bean);
            Method messageFilterMethodToUse = checkProxy(messageFilterMethod, bean);
            endpoint.setMessageFilterMethod(messageFilterMethodToUse);
        }
        endpoint.setMessageHandlerMethodFactory(getMessageHandlerMethodFactory());
        endpoint.setMessageSelectors(resolveMessageSelectors(centralListener));
        this.centralListenerEndpointRegistry.registerEndpoint(endpoint);
        if (StringUtils.hasText(beanRef)) {
            this.listenerScope.removeListener(beanRef);
        }
    }

    private MessageHandlerMethodFactory getMessageHandlerMethodFactory() {
        if (this.messageHandlerMethodFactory == null) {
            this.messageHandlerMethodFactory = createDefaultMessageHandlerMethodFactory();
        }
        return this.messageHandlerMethodFactory;
    }

    private MessageHandlerMethodFactory createDefaultMessageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
        defaultFactory.setBeanFactory(this.beanFactory);

        ConfigurableBeanFactory cbf =
                this.beanFactory instanceof ConfigurableBeanFactory ? (ConfigurableBeanFactory) this.beanFactory : null;


        this.defaultFormattingConversionService.addConverter(
                new BytesToStringConverter(this.charset));

        defaultFactory.setConversionService(this.defaultFormattingConversionService);

        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

        // Annotation-based argument resolution
        argumentResolvers.add(new HeaderMethodArgumentResolver(this.defaultFormattingConversionService, cbf));
        argumentResolvers.add(new HeadersMethodArgumentResolver());

        // Type-based argument resolution
        final GenericMessageConverter messageConverter =
                new GenericMessageConverter(this.defaultFormattingConversionService);
        argumentResolvers.add(new MessageMethodArgumentResolver(messageConverter));
        MessageConverter compositeMessageConverter = new CompositeMessageConverter(List.of(messageConverter, new MappingJackson2MessageConverter(new MimeType[]{})));
        argumentResolvers.add(new PayloadArgumentResolver(compositeMessageConverter, null) {


            @Override
            public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
                Object resolved = super.resolveArgument(parameter, message);
                /*
                 * Replace KafkaNull list elements with null.
                 */
                if (resolved instanceof List) {
                    List<?> list = ((List<?>) resolved);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof KafkaNull) {
                            list.set(i, null);
                        }
                    }
                }
                return resolved;
            }

            @Override
            protected boolean isEmptyPayload(Object payload) {
                return payload == null || payload instanceof KafkaNull;
            }

        });
        defaultFactory.setArgumentResolvers(argumentResolvers);

        defaultFactory.afterPropertiesSet();
        return defaultFactory;
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @KafkaListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    }
                    catch (NoSuchMethodException ignored) {
                    }
                }
            }
            catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@KafkaListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(),
                        method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }


    private CentralListener findListenerAnnotations(Method method) {
        return AnnotatedElementUtils.findMergedAnnotation(method, CentralListener.class);
    }

    private String[] resolveMessageSelectors(CentralListener centralListener) {
        String[] messageSelectors = centralListener.messageSelectors();
        List<String> result = new ArrayList<>();
        if (messageSelectors.length > 0) {
            for (String messageSelector1 : messageSelectors) {
                Object messageSelector = resolveExpression(messageSelector1);
                resolveAsString(messageSelector, result);
            }
        }
        return result.toArray(new String[0]);
    }

    private Method getMessageFilterMethod(CentralListener centralListener, Object bean) {
        String messageFilter = centralListener.messageFilter();
        Set<Method> methods = MethodIntrospector.selectMethods(bean.getClass(), (ReflectionUtils.MethodFilter) method -> method.getName().equals(messageFilter));
        Assert.notEmpty(methods, "Message filter points to non-existent method.");
        Assert.isTrue(methods.size() == 1, "Message filter expression matches more than one method.");
        Method method = methods.iterator().next();
        Assert.isTrue(boolean.class.equals(method.getReturnType()), "Message filter method should return boolean");
        return method;
    }

    private Object resolveExpression(String value) {
        return this.resolver.evaluate(resolve(value), this.expressionContext);
    }

    @SuppressWarnings("unchecked")
    private void resolveAsString(Object resolvedValue, List<String> result) {
        if (resolvedValue instanceof String[]) {
            for (Object object : (String[]) resolvedValue) {
                resolveAsString(object, result);
            }
        }
        else if (resolvedValue instanceof String) {
            result.add((String) resolvedValue);
        }
        else if (resolvedValue instanceof Iterable) {
            for (Object object : (Iterable<Object>) resolvedValue) {
                resolveAsString(object, result);
            }
        }
        else {
            throw new IllegalArgumentException(String.format(
                    "@KafKaListener can't resolve '%s' as a String", resolvedValue));
        }
    }

    private String resolve(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    private static class ListenerScope implements Scope {

        private final Map<String, Object> listeners = new HashMap<>();

        ListenerScope() {
            super();
        }

        public void addListener(String key, Object bean) {
            this.listeners.put(key, bean);
        }

        public void removeListener(String key) {
            this.listeners.remove(key);
        }

        @Override
        public Object get(String name, ObjectFactory<?> objectFactory) {
            return this.listeners.get(name);
        }

        @Override
        public Object remove(String name) {
            return null;
        }

        @Override
        public void registerDestructionCallback(String name, Runnable callback) {
        }

        @Override
        public Object resolveContextualObject(String key) {
            return this.listeners.get(key);
        }

        @Override
        public String getConversationId() {
            return null;
        }

    }

    private static class BytesToStringConverter implements Converter<byte[], String> {
        private final Charset charset;

        BytesToStringConverter(Charset charset) {
            this.charset = charset;
        }

        @Override
        public String convert(byte[] source) {
            return new String(source, this.charset);
        }

    }

}
