package com.freshworks.boot.samples.common.service;

import com.freshworks.boot.common.persistence.NotFoundException;
import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.repository.TodoRepository;
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

    public void deleteTodo(long todoId) {
        Todo todo = getTodo(todoId);
        if (todo == null) {
            throw new NotFoundException("todo_id");
        }
        todoRepository.delete(todo);
    }
}
