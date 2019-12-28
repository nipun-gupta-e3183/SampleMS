package com.freshworks.starter.sample.kafka_processor.kafka;

import com.freshworks.starter.sample.kafka_processor.config.MessageKey;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BootKafkaListener {
    private final Logger logger = LoggerFactory.getLogger(BootKafkaListener.class);

    private final MethodCentralListenerEndpointRegistry centralListenerEndpointRegistry;

    public BootKafkaListener(MethodCentralListenerEndpointRegistry centralListenerEndpointRegistry) {
        this.centralListenerEndpointRegistry = centralListenerEndpointRegistry;
    }

    @KafkaListener(topics = "${kafka.topics}", groupId = "${kafka.consumerGroupId}", containerFactory = "kafkaListenerContainerFactory")
    public void process(List<Message<String>> messages, Acknowledgment acknowledgment, Consumer<MessageKey, String> consumer) {
        for (Message<String> message : messages) {
            MessageKey messageKey = message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, MessageKey.class);
            if (centralListenerEndpointRegistry.hasHandler(messageKey)) {
                @SuppressWarnings("ConstantConditions")
                InvocableHandlerMethod handlerMethod = centralListenerEndpointRegistry.getHandlerMethod(messageKey);
                try {
                    handlerMethod.invoke(new GenericMessage<>(message.getPayload(), message.getHeaders()), messageKey, consumer);
                } catch (Exception e) {
                    //TODO: Handle retryable & non-retryable errors differently. Implement retries.
                    String errorMessage = String.format(
                            "Couldn't handle message at topic: %s, partition: %s, offset: %s, with key: %s. Ignoring the message & continuing.",
                            message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                            message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                            message.getHeaders().get(KafkaHeaders.OFFSET),
                            messageKey);
                    logger.error(errorMessage);
                }
            } else {
                String debugMessage = String.format(
                        "No listener configured for message at topic: %s, partition: %s, offset: %s, with key: %s. Ignoring the message & continuing.",
                        message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                        message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                        message.getHeaders().get(KafkaHeaders.OFFSET),
                        messageKey);
                logger.debug(debugMessage);
            }
        }
        try {
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logger.error("Exception occurred while acknowledging. Ignoring.");
        }
    }

}
