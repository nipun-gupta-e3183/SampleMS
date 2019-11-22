package com.freshworks.starter.sample.api;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.repository.TodoRepository;
import com.freshworks.starter.web.security.PermissionsDecoder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//TODO: Component scan for "com.freshworks.boot.rest" should be handled by auto-configuration
@SpringBootApplication(scanBasePackages = {"com.freshworks.starter.sample", "com.freshworks.boot.rest"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class ApiApplication {
    @Bean
    public PermissionsDecoder permissionsDecoder() {
        return new PermissionsDecoder(getClass().getResourceAsStream("/permissions.properties"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
