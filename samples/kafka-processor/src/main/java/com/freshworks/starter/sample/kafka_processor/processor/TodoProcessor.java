package com.freshworks.starter.sample.kafka_processor.processor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.kafka_processor.config.MessageKey;
import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import com.freshworks.starter.sample.kafka_processor.mapper.TodoMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TodoProcessor {
    private final Logger logger = LoggerFactory.getLogger(TodoProcessor.class);
    private final TodoService todoService;
    private final ObjectMapper objectMapper;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${kafka.topics}", groupId = "${kafka.consumerGroupId}", containerFactory = "kafkaListenerContainerFactory")
    public void process(ConsumerRecords<MessageKey, String> consumerRecords, Acknowledgment acknowledgment) {
        for (ConsumerRecord<MessageKey, String> consumerRecord : consumerRecords) {
            MessageKey messageKey = consumerRecord.key();
            if ("todo_create".equals(messageKey.getPayloadType())) {
                TodoDto todoDto;
                try {
                    todoDto = objectMapper.readValue(consumerRecord.value(), TodoDto.class);
                } catch (IOException e) {
                    String message = String.format("Couldn't parse message at topic: %s, partition: %d, offset: %d, with key: %s. Ignoring the message & continuing.", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), messageKey);
                    logger.error(message);
                    continue;
                }
                boolean failed = false;
                do {
                    try {
                        todoService.addTodo(todoMapper.convert(todoDto));
                    } catch (Exception e) {
                        failed = true;
                        try {
                            Thread.sleep(100); //TODO: Exponential back-off
                        } catch (InterruptedException ex) {
                            logger.error("Thread interrupted while waiting for retrying. Exiting");
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                } while (failed);
            }
        }
        try {
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logger.error("Exception occurred while acknowledging. Ignoring.");
        }
    }

}
