package com.freshworks.starter.sample.kafka_processor.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<MessageKey, String> todoConsumerFactory(KafkaProperties properties) {
        Map<String, Object> config = properties.buildConsumerProperties();
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, MessageKeyDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config, new MessageKeyDeserializer(), new StringDeserializer());
    }

    // Note: One bean should be named "kafkaListenerContainerFactory" as spring-boot requires this.
    @Bean(name="kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<MessageKey, String> todoKafkaListenerFactory(KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<MessageKey, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(todoConsumerFactory(properties));
        return factory;
    }
}
