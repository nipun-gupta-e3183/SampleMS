package com.freshworks.boot.samples.kafka_processor.processor;

import com.freshworks.boot.kafka.CentralListener;
import com.freshworks.boot.kafka.MessageKey;
import com.freshworks.boot.kafka.dto.central.EventChangesDto;
import com.freshworks.boot.kafka.dto.central.EventDto;
import com.freshworks.boot.samples.common.service.TodoService;
import com.freshworks.boot.samples.kafka_processor.dto.TodoCreateDto;
import com.freshworks.boot.samples.kafka_processor.dto.TodoDeleteDto;
import com.freshworks.boot.samples.kafka_processor.dto.TodoDto;
import com.freshworks.boot.samples.kafka_processor.mapper.TodoMapper;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    public boolean isEligible(MessageKey messageKey) {
        return Integer.parseInt(messageKey.getAccountId()) % 2 != 0;
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @CentralListener(messageSelectors = "todo:todo_create:*", messageFilterEnabled = true, messageFilter = "isEligible")
    public void processCreate(EventDto<TodoCreateDto, Void, Void, EventChangesDto<Void, Void, Void>> todoDto, @SuppressWarnings("unused") MessageKey messageKey) {
        todoService.addTodo(todoMapper.convert(todoDto.getData().getPayload().getModelProperties()));
    }

    @CentralListener(messageSelectors = "todo:todo_update:*")
    public void processUpdate(EventDto<TodoDto, Void, Void, EventChangesDto<Void, Void, Void>> todoDto) {
        todoService.updateTodo(todoMapper.convert(todoDto.getData().getPayload().getModelProperties()));
    }

    /* With specific version for payload_version part */
    @CentralListener(messageSelectors = "todo:todo_delete:1.0.0", messageFilterEnabled = false)
    public void processDelete(EventDto<TodoDeleteDto, Void, Void, EventChangesDto<Void, Void, Void>> todoDeleteDto) {
        todoService.deleteTodo(todoDeleteDto.getData().getPayload().getModelProperties().getId());
    }

}
