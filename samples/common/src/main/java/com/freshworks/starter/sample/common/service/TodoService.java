package com.freshworks.starter.sample.common.service;

import com.freshworks.boot.common.persistence.NotFoundException;
import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.repository.TodoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listAllTodos() {
        return todoRepository.findAll();
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo getTodo(long todoId) {
        return todoRepository.findById(todoId).orElseThrow(() -> new NotFoundException("todo_id"));
    }

    public Todo updateTodo(Todo updateTodo) {
        getTodo(updateTodo.getId());
        return todoRepository.save(updateTodo);
    }
}
