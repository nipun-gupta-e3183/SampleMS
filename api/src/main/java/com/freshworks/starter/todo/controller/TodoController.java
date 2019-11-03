package com.freshworks.starter.todo.controller;

import com.freshworks.starter.todo.model.Todo;
import com.freshworks.starter.todo.service.TodoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path = "/api/v1/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('todo:list')")
    public List<Todo> listTodos() {
        return todoService.listAllTodos();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('todo:create')")
    public Todo addTodo(@RequestBody @Valid Todo newTodo) {
        return todoService.addTodo(newTodo);
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("hasAuthority('todo:update')")
    public Todo updateTodo(@PathVariable long todoId, @RequestBody @Valid Todo updateTodo) {
        updateTodo.setId(todoId);
        return todoService.updateTodo(updateTodo);
    }

    @GetMapping("/{todoId}")
    @PreAuthorize("hasAuthority('todo:get')")
    public Todo getTodo(@PathVariable long todoId) {
        return todoService.getTodo(todoId);
    }

}
