package com.freshworks.starter.sample.sqs_processor.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

/**
 * Configuration class for integration test that do not need message listening capabilities.
 */
@TestConfiguration
public class MockMessageListenerConfiguration {

    @MockBean
    private SimpleMessageListenerContainer messageListenerContainer;

}