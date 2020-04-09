package com.freshworks.boot.samples.api.v1.mapper;

import com.freshworks.boot.samples.api.v1.dto.TodoCreateDto;
import com.freshworks.boot.samples.api.v1.dto.TodoDto;
import com.freshworks.boot.samples.common.model.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto convert(Todo todo);
    Todo convert(TodoDto todo);
    @Mapping(target = "id", ignore = true)
    Todo convert(TodoCreateDto todo);
    List<TodoDto> convert(List<Todo> todos);
}
