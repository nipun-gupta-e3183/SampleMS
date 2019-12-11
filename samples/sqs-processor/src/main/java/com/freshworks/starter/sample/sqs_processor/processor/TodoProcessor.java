package com.freshworks.starter.sample.sqs_processor.processor;

import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.sqs_processor.dto.TodoDto;
import com.freshworks.starter.sample.sqs_processor.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    @Value("sqs.queueName")
    private String queueName;

    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @SqsListener(value = "todos", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void process(TodoDto todoDto) {
        todoService.addTodo(todoMapper.convert(todoDto));
    }

}
