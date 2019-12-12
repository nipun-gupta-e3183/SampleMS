package com.freshworks.starter.sample.kafka_processor.processor;

import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import com.freshworks.starter.sample.kafka_processor.mapper.TodoMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @KafkaListener(topics = "todos", groupId = "sample-kafka-processor", containerFactory = "kafkaListenerContainerFactory")
    public void process(TodoDto todoDto) {
        todoService.addTodo(todoMapper.convert(todoDto));
    }

}
