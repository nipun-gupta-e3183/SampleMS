package com.freshworks.starter.sample.kafka_processor.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;

public class MessageKeyDeserializer implements Deserializer<MessageKey> {
    private StringDeserializer stringDeserializer = new StringDeserializer();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        stringDeserializer.configure(configs, isKey);
    }

    @Override
    public MessageKey deserialize(String topic, byte[] data) {
        String keyString = stringDeserializer.deserialize(topic, data);
        //service#payload_type#account_id#msg_id#payload_version
        String[] parts = keyString.split("#");
        return new MessageKey(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    @Override
    public void close() {
        stringDeserializer.close();
    }
}
