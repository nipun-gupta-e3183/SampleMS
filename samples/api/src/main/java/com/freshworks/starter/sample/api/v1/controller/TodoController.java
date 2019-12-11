package com.freshworks.starter.sample.api.v1.controller;

import com.freshworks.starter.sample.api.v1.dto.TodoCreateDto;
import com.freshworks.starter.sample.api.v1.dto.TodoDto;
import com.freshworks.starter.sample.api.v1.dto.TodoListResponseDto;
import com.freshworks.starter.sample.api.v1.dto.TodoListResponseMetaDto;
import com.freshworks.starter.sample.api.v1.mapper.TodoMapper;
import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public TodoListResponseDto listTodos() {
        List<Todo> todos = todoService.listAllTodos();
        return new TodoListResponseDto().todos(todoMapper.convert(todos))
                .meta(new TodoListResponseMetaDto()
                        .count(todos.size()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('/todo/create')")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto addTodo(@RequestBody @Valid TodoCreateDto newTodoDto) {
        Todo todo = todoMapper.convert(newTodoDto);
        return todoMapper.convert(todoService.addTodo(todo));
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/update')")
    public TodoDto updateTodo(@PathVariable long todoId, @RequestBody @Valid TodoCreateDto updateTodoDto) {
        Todo todo = todoMapper.convert(updateTodoDto);
        todo.setId(todoId);
        return todoMapper.convert(todoService.updateTodo(todo));
    }

    @GetMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/get')")
    public TodoDto getTodo(@PathVariable long todoId) {
        return todoMapper.convert(todoService.getTodo(todoId));
    }

    @DeleteMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/delete')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable long todoId) {
        todoService.deleteTodo(todoId);
    }

}
