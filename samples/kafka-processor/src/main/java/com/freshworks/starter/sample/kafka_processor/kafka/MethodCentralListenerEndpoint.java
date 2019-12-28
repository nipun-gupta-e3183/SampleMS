package com.freshworks.starter.sample.kafka_processor.kafka;

import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.lang.reflect.Method;

public class MethodCentralListenerEndpoint {
    private Object bean;
    private Method method;
    private Method messageFilterMethod;
    private MessageHandlerMethodFactory messageHandlerMethodFactory;
    private String[] messageSelectors;

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setMessageFilterMethod(Method messageFilterMethod) {
        this.messageFilterMethod = messageFilterMethod;
    }

    public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.messageHandlerMethodFactory = messageHandlerMethodFactory;
    }

    public void setMessageSelectors(String[] messageSelectors) {
        this.messageSelectors = messageSelectors;
    }

    public String[] getMessageSelectors() {
        return messageSelectors;
    }

    public HandlerMethods getMessageHandler() {
        InvocableHandlerMethod messageHandlerMethod = messageHandlerMethodFactory.createInvocableHandlerMethod(bean, method);
        InvocableHandlerMethod messageFilterHandlerMethod = null;
        if (messageFilterMethod != null) {
            messageFilterHandlerMethod = messageHandlerMethodFactory.createInvocableHandlerMethod(bean, messageFilterMethod);
        }
        return new HandlerMethods(messageHandlerMethod, messageFilterHandlerMethod);
    }
}
