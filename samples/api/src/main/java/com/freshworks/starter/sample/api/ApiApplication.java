package com.freshworks.starter.sample.api;

import com.freshworks.starter.sample.api.security.PermissionsDecoder;
import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.repository.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.freshworks.starter.sample.common", "com.freshworks.starter.sample.api"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class ApiApplication {
    private Map<Integer, String> PERMISSIONS_BIT_SET = Map.of(
            1, "todo:list",
            2, "todo:get",
            3, "todo:create",
            4, "todo:update",
            5, "todo:delete"
    );

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PermissionsDecoder permissionsDecoder() {
        return new PermissionsDecoder(PERMISSIONS_BIT_SET);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
