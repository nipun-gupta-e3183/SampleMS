package com.freshworks.starter.sample.kafka_processor.config;

import java.util.Objects;

public class MessageKey {
    private final String service;
    private final String payloadType;
    private final String accountId;
    private final String msgId;
    private final String payloadVersion;

    public MessageKey(String service, String payloadType, String accountId, String msgId, String payloadVersion) {
        this.service = service;
        this.payloadType = payloadType;
        this.accountId = accountId;
        this.msgId = msgId;
        this.payloadVersion = payloadVersion;
    }

    public String getService() {
        return service;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getPayloadVersion() {
        return payloadVersion;
    }

    @Override
    public String toString() {
        return "MessageKey{" +
                "service='" + service + '\'' +
                ", payloadType='" + payloadType + '\'' +
                ", accountId='" + accountId + '\'' +
                ", msgId='" + msgId + '\'' +
                ", payloadVersion='" + payloadVersion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageKey)) return false;
        MessageKey that = (MessageKey) o;
        return service.equals(that.service) &&
                payloadType.equals(that.payloadType) &&
                accountId.equals(that.accountId) &&
                msgId.equals(that.msgId) &&
                payloadVersion.equals(that.payloadVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, payloadType, accountId, msgId, payloadVersion);
    }
}
