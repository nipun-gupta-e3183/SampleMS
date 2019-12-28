package com.freshworks.starter.sample.kafka_processor.mapper;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.kafka_processor.dto.TodoCreateDto;
import com.freshworks.starter.sample.kafka_processor.dto.TodoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convertToTodoDto(Todo todo);
    Todo convert(TodoDto todo);
    TodoCreateDto convertToTodoCreateDto(Todo todo);
    Todo convert(TodoCreateDto todo);
}
