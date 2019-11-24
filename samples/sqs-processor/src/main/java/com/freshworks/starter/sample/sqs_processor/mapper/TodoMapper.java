package com.freshworks.starter.sample.sqs_processor.mapper;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.sqs_processor.dto.TodoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
}
