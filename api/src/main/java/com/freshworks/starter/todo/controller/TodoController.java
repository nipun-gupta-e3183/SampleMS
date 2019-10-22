package com.freshworks.starter.todo.controller;

import com.freshworks.starter.todo.model.Todo;
import com.freshworks.starter.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/api/v1/todos")
    @PreAuthorize("hasAuthority('admin')")
    public List<Todo> listTodos() {
        return todoService.listAllTodos();
    }

    @PostMapping("/api/v1/todos")
    public ResponseEntity<Void> addTodo(@RequestBody Todo newTodo) {

        Todo todo = todoService.addTodo(newTodo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(todo.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/api/v1/todos/{todoId}")
    public Todo getTodo(@PathVariable long todoId) {
        return todoService.getTodo(todoId);
    }

}
