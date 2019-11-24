package com.freshworks.starter.sample.api.mapper;

import com.freshworks.starter.sample.api.dto.TodoDto;
import com.freshworks.starter.sample.common.model.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
}
