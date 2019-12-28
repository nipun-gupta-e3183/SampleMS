package com.freshworks.starter.sample.kafka_processor.processor;

import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.kafka_processor.config.MessageKey;
import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import com.freshworks.starter.sample.kafka_processor.kafka.CentralListener;
import com.freshworks.starter.sample.kafka_processor.mapper.TodoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final Logger logger = LoggerFactory.getLogger(TodoProcessor.class);
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    public boolean isEligible(MessageKey messageKey) {
        return true;
    }

    @CentralListener(messageSelectors = "freshdesk:todo_create:1.0.0")
    public void process(TodoDto todoDto, MessageKey messageKey) {
        todoService.addTodo(todoMapper.convert(todoDto));
    }

}
