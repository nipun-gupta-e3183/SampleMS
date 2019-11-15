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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.freshworks.starter.sample"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class ApiApplication {
    @Bean
    public PermissionsDecoder permissionsDecoder() {
        Properties permissions = new Properties();
        try {
            permissions.load(getClass().getResourceAsStream("/permissions.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, String> indexToPermissionsMap = permissions.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt((String) e.getKey()), e -> (String) e.getValue()));
        return new PermissionsDecoder(indexToPermissionsMap);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
