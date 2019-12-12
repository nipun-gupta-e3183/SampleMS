package com.freshworks.starter.sample.kafka_processor.config;

import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, TodoDto> todoConsumerFactory(KafkaProperties properties) {
        Map<String, Object> config = properties.buildConsumerProperties();
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(TodoDto.class));
    }

    // Note: One bean should be named "kafkaListenerContainerFactory" as spring-boot requires this.
    @Bean(name="kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TodoDto> todoKafkaListenerFactory(KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<String, TodoDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(todoConsumerFactory(properties));
        return factory;
    }
}
