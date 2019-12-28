package com.freshworks.starter.sample.kafka_processor.kafka;

import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method to be the target of a Kafka message listener for the specified messages.
 *
 * <p>
 * Annotated methods are allowed to have flexible signatures similar to what
 * {@link MessageMapping} provides, that is
 * <ul>
 * <li>{@link org.springframework.messaging.handler.annotation.Payload @Payload}-annotated
 * method arguments including the support of validation. If these arguments are not String, the message payload will be
 * JSON de-serialized into the specified type's instance. The
 * {@link org.springframework.messaging.handler.annotation.Payload @Payload} annotation is optional.</li>
 * <li>{@link com.freshworks.starter.sample.kafka_processor.config.MessageKey MessageKey} argument for getting
 * access to the message key.</li>
 * <li>{@link org.springframework.messaging.handler.annotation.Header @Header}-annotated
 * method arguments to extract a specific header value, defined by
 * {@link org.springframework.kafka.support.KafkaHeaders KafkaHeaders}</li>
 * <li>{@link org.springframework.messaging.handler.annotation.Headers @Headers}-annotated
 * argument that must also be assignable to {@link java.util.Map} for getting access to
 * all headers.</li>
 * <li>{@link org.springframework.messaging.MessageHeaders MessageHeaders} arguments for
 * getting access to all headers.</li>
 * <li>{@link org.springframework.messaging.support.MessageHeaderAccessor
 * MessageHeaderAccessor} for convenient access to all method arguments.</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CentralListener {
    /**
     * The selectors for messages to be sent to this listener.
     * The selectors should be in the form {@code service:payload_type:payload_version} format. The
     * {@code payload_version} part can be wildcard (*) too for matching all versions. The entries can be
     * literals, 'property-placeholder keys' or 'expressions'.
     * An expression must be resolved to the literal structure outlined above.
     * @return the selectors or expressions (SpEL) identifying messages to listen to.
     */
    String[] messageSelectors();

    /**
     * A pseudo bean name used in SpEL expressions within this annotation to reference
     * the current bean within which this listener is defined. This allows access to
     * properties and methods within the enclosing bean.
     * Default '__listener'.
     * <p>
     * Example: {@code messageSelectors = "#{__listener.messageSelectorList}"}.
     * @return the pseudo bean name.
     * @since 2.1.2
     */
    String beanRef() default "__listener";

    /**
     * Whether message filter method is available or not. If this returns false, {@link #messageFilter()} configuration
     * will not be ignored and no filtering will be performed. If this returns true, for each message, method identified
     * by {@link #messageFilter()}  will be invoked.
     * @return the name of the messageFilter method
     */
    boolean messageFilterEnabled() default true;

    /**
     * The name of the method, defined in the listener class, that can check if a message is of interest to the listener.
     * The method can accept all arguments valid for listener method, however, must return boolean. If the method returned false
     * for a message, that message will be ignored. If it returns true, the listener method will be invoked.
     * @return the name of the messageFilter method
     */
    String messageFilter() default "isEligible";

}
