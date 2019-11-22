package com.freshworks.starter.sample.sqs_processor.processor;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.service.TodoService;
import com.freshworks.starter.sample.sqs_processor.dto.TodoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    @Value("sqs.queueName")
    private String queueName;

    private final TodoService todoService;
    private ModelMapper modelMapper;

    public TodoProcessor(TodoService todoService, ModelMapper modelMapper) {
        this.todoService = todoService;
        this.modelMapper = modelMapper;
    }

    @SqsListener("todos")
    public void process(TodoDto todoDto) {
        todoService.addTodo(convertToEntity(todoDto));
    }

    private TodoDto convertToDto(Todo todo) {
        return modelMapper.map(todo, TodoDto.class);
    }

    private Todo convertToEntity(TodoDto todoDto) {
        return modelMapper.map(todoDto, Todo.class);
    }

}
