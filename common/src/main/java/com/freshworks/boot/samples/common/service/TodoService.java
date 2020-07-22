package com.freshworks.boot.samples.common.service;

import com.freshworks.boot.common.context.account.AccountContext;
import com.freshworks.boot.common.persistence.NotFoundException;
import com.freshworks.boot.samples.common.model.Account;
import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.repository.TodoRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class TodoService {

    private final TodoRepository todoRepository;
    private AccountContext<Account> accountContext;

    @PersistenceContext
    public EntityManager entityManager;

    public TodoService(TodoRepository todoRepository, AccountContext<Account> accountContext) {
        this.todoRepository = todoRepository;
        this.accountContext = accountContext;
    }

    public List<Todo> listAllTodos() {
        return todoRepository.findByAccountId(getAccountID());
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }


    public Todo getTodo(long todoId) {
        return todoRepository.findByAccountIdAndId(getAccountID(), todoId).orElseThrow(() -> new NotFoundException("todo_id"));
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

    public String getAccountID() {
        return accountContext.get().getAccountID();
    }

}
