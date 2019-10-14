package com.freshworks.fd.starter.todo.service;

import com.freshworks.fd.starter.todo.model.Todo;
import com.freshworks.fd.starter.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return todoRepository.getOne(todoId);
    }
}
