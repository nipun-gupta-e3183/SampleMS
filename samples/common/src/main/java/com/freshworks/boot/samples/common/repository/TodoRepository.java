package com.freshworks.boot.samples.common.repository;

import com.freshworks.boot.samples.common.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByAccountId(String accountId);

    Optional<Todo> findByAccountIdAndId(String accountId, long todoId);
}
