package com.freshworks.starter.sample.kafka_processor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.kafka_processor.config.MessageKey;
import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import com.freshworks.starter.sample.kafka_processor.mapper.TodoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TodoProcessor {
    private final Logger logger = LoggerFactory.getLogger(TodoProcessor.class);
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @KafkaListener(topics = "${kafka.topics}", groupId = "${kafka.consumerGroupId}", containerFactory = "kafkaListenerContainerFactory")
    public void process(String data, @Header(name = "kafka_receivedMessageKey") MessageKey key) {
        if ("todo_create".equals(key.getPayloadType())) {
            ObjectMapper objectMapper = new ObjectMapper();
            TodoDto todoDto = null;
            try {
                todoDto = objectMapper.readValue(data, TodoDto.class);
                todoService.addTodo(todoMapper.convert(todoDto));
            } catch (IOException e) {
                logger.warn("Couldn't parse message with key: {}", key);
            }
        }
    }

}
