package com.freshworks.starter.sample.kafka_processor.kafka;

import com.freshworks.starter.sample.kafka_processor.config.MessageKey;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Component
public class MethodCentralListenerEndpointRegistry {
    private static final String VERSION_WILDCARD = "*";

    // Map<service, Map<payloadType, Map<payloadVersion, InvocableHandlerMethod>>>
    private final Map<String, Map<String, Map<String, InvocableHandlerMethod>>> handlerMethods = new HashMap<>();

    public boolean hasHandler(MessageKey messageKey) {
        return getHandlerMethod(messageKey) != null;
    }

    public InvocableHandlerMethod getHandlerMethod(MessageKey messageKey) {
        Map<String, Map<String, InvocableHandlerMethod>> serviceMap = handlerMethods.computeIfAbsent(messageKey.getService(), s -> new HashMap<>());
        Map<String, InvocableHandlerMethod> payloadTypeMap = serviceMap.computeIfAbsent(messageKey.getPayloadType(), s -> new HashMap<>());
        //TODO: Support partial wildcard in payloadVersion like `1.1.*` & also relative expressions like `<1.1.0`
        InvocableHandlerMethod handlerMethod = payloadTypeMap.get(messageKey.getPayloadVersion());
        if (handlerMethod != null) {
            return handlerMethod;
        }
        return payloadTypeMap.get(VERSION_WILDCARD);
    }

    public void registerEndpoint(MethodCentralListenerEndpoint endpoint) {
        Assert.notNull(endpoint, "Endpoint must be set");
        Assert.notEmpty(endpoint.getMessageSelectors(), "Message selectors must be set");
        String[] messageSelectors = endpoint.getMessageSelectors();
        for (String messageSelector : messageSelectors) {
            String[] parts = messageSelector.split(":");
            Assert.isTrue(parts.length == 3,
                    "Message selectors should contain exactly 3 parts separated by ':'");
            Map<String, Map<String, InvocableHandlerMethod>> serviceMap = handlerMethods.computeIfAbsent(parts[0], s -> new HashMap<>());
            Map<String, InvocableHandlerMethod> payloadTypeMap = serviceMap.computeIfAbsent(parts[1], s -> new HashMap<>());
            Assert.isNull(payloadTypeMap.get(parts[2]), "Duplicate message selector");
            payloadTypeMap.put(parts[2], endpoint.getMessageHandler());
        }
    }
}
