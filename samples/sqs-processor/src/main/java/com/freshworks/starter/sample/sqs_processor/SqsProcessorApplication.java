package com.freshworks.starter.sample.sqs_processor;

import com.freshworks.starter.sample.common.model.Todo;
import com.freshworks.starter.sample.common.repository.TodoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"com.freshworks.starter.sample"})
@EntityScan(basePackageClasses = Todo.class)
@EnableJpaRepositories(basePackageClasses = TodoRepository.class)
public class SqsProcessorApplication {

    //TODO: Move this dependency into SQS starter module
    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

        //set strict content type match to false
        messageConverter.setStrictContentTypeMatch(false);
        factory.setArgumentResolvers(Collections.singletonList(new PayloadArgumentResolver(messageConverter)));
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(SqsProcessorApplication.class, args);
    }

}
