package com.freshworks.starter.sample.kafka_processor;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.repository.TodoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.freshworks.starter.sample"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class KafkaProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProcessorApplication.class, args);
    }

}
