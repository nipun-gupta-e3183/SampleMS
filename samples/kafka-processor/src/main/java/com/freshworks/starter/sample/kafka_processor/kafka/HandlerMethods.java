package com.freshworks.starter.sample.kafka_processor.kafka;

import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

public class HandlerMethods {
    private final InvocableHandlerMethod listenerMethod;
    private final InvocableHandlerMethod messageFilterMethod;

    public HandlerMethods(InvocableHandlerMethod listenerMethod, InvocableHandlerMethod messageFilterMethod) {
        this.listenerMethod = listenerMethod;
        this.messageFilterMethod = messageFilterMethod;
    }

    public InvocableHandlerMethod getListenerMethod() {
        return listenerMethod;
    }

    public InvocableHandlerMethod getMessageFilterMethod() {
        return messageFilterMethod;
    }
}
