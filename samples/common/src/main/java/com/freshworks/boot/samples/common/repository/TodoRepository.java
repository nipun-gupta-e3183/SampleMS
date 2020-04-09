package com.freshworks.boot.samples.common.repository;

import com.freshworks.boot.samples.common.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
