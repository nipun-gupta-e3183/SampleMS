package com.freshworks.boot.samples.sqs_processor.mapper;

import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.sqs_processor.dto.TodoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
}
