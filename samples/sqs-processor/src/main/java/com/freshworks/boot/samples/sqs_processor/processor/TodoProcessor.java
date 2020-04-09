package com.freshworks.boot.samples.sqs_processor.processor;

import com.freshworks.boot.samples.common.service.TodoService;
import com.freshworks.boot.samples.sqs_processor.dto.TodoDto;
import com.freshworks.boot.samples.sqs_processor.mapper.TodoMapper;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @SqsListener(value = "${sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void process(TodoDto todoDto) {
        todoService.addTodo(todoMapper.convert(todoDto));
    }

}
