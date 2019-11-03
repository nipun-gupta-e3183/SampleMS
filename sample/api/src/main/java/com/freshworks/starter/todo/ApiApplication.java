package com.freshworks.starter.todo;

import com.freshworks.starter.todo.security.PermissionsDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

@SpringBootApplication
public class ApiApplication {
	private Map<Integer, String> PERMISSIONS_BIT_SET = Map.of(
			1, "todo:list",
			2, "todo:create"
	);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public PermissionsDecoder permissionsDecoder() {
		return new PermissionsDecoder(PERMISSIONS_BIT_SET);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
