package com.freshworks.starter.sample.api.v1.mapper;

import com.freshworks.starter.sample.api.v1.dto.TodoCreateDto;
import com.freshworks.starter.sample.api.v1.dto.TodoDto;
import com.freshworks.starter.sample.common.model.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
    Todo convert(TodoCreateDto todo);
    List<TodoDto> convert(List<Todo> todos);
}
