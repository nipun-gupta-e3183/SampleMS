package com.freshworks.fd.starter.todo.api;

import com.freshworks.fd.starter.todo.repository.TodoRepository;
import com.freshworks.fd.starter.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
