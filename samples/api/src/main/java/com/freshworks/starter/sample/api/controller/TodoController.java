package com.freshworks.starter.sample.api.controller;

import com.freshworks.starter.sample.api.dto.TodoDto;
import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(path = "/api/v1/todos")
public class TodoController {
    private final TodoService todoService;
    private ModelMapper modelMapper;

    public TodoController(TodoService todoService, ModelMapper modelMapper) {
        this.todoService = todoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('todo:list')")
    public List<TodoDto> listTodos() {
        List<Todo> todos = todoService.listAllTodos();
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('todo:create')")
    public TodoDto addTodo(@RequestBody @Valid TodoDto newTodoDto) {
        Todo todo = convertToEntity(newTodoDto);
        return convertToDto(todoService.addTodo(todo));
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("hasAuthority('todo:update')")
    public TodoDto updateTodo(@PathVariable long todoId, @RequestBody @Valid TodoDto updateTodoDto) {
        updateTodoDto.setId(todoId);
        return convertToDto(todoService.updateTodo(convertToEntity(updateTodoDto)));
    }

    @GetMapping("/{todoId}")
    @PreAuthorize("hasAuthority('todo:get')")
    public TodoDto getTodo(@PathVariable long todoId) {
        return convertToDto(todoService.getTodo(todoId));
    }

    private TodoDto convertToDto(Todo todo) {
        return modelMapper.map(todo, TodoDto.class);
    }
    private Todo convertToEntity(TodoDto todoDto) {
        return modelMapper.map(todoDto, Todo.class);
    }

}
