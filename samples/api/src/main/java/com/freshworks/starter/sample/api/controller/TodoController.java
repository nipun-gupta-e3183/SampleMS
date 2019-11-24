package com.freshworks.starter.sample.api.controller;

import com.freshworks.starter.sample.api.dto.TodoDto;
import com.freshworks.starter.sample.api.mapper.TodoMapper;
import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.service.TodoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(path = "/api/v1/todos")
public class TodoController {
    private final TodoService todoService;
    private TodoMapper todoMapper;

    public TodoController(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('/todo/list')")
    public List<TodoDto> listTodos() {
        List<Todo> todos = todoService.listAllTodos();
        return todos.stream().map(t -> todoMapper.convert(t)).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('/todo/create')")
    public TodoDto addTodo(@RequestBody @Valid TodoDto newTodoDto) {
        Todo todo = todoMapper.convert(newTodoDto);
        return todoMapper.convert(todoService.addTodo(todo));
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/update')")
    public TodoDto updateTodo(@PathVariable long todoId, @RequestBody @Valid TodoDto updateTodoDto) {
        updateTodoDto.setId(todoId);
        return todoMapper.convert(todoService.updateTodo(todoMapper.convert(updateTodoDto)));
    }

    @GetMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/get')")
    public TodoDto getTodo(@PathVariable long todoId) {
        return todoMapper.convert(todoService.getTodo(todoId));
    }

}
