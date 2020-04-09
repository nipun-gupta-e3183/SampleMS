package com.freshworks.boot.samples.kafka_processor.mapper;

import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.kafka_processor.dto.TodoCreateDto;
import com.freshworks.boot.samples.kafka_processor.dto.TodoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
    Todo convert(TodoCreateDto todo);
}
