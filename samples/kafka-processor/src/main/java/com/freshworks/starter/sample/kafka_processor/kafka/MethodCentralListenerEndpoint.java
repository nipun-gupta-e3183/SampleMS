package com.freshworks.starter.sample.kafka_processor.kafka;

import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.lang.reflect.Method;

public class MethodCentralListenerEndpoint {
    private Object bean;
    private Method method;
    private MessageHandlerMethodFactory messageHandlerMethodFactory;
    private String[] messageSelectors;

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setMethod(Method method) {
        this.method = method;
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

    public InvocableHandlerMethod getMessageHandler() {
        return messageHandlerMethodFactory.createInvocableHandlerMethod(bean, method);
    }
}
