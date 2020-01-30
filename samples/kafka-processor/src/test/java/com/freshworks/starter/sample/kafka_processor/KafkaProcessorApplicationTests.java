package com.freshworks.starter.sample.kafka_processor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SuppressWarnings("WeakerAccess")
@SpringBootTest
@AutoConfigureTestDatabase
@EnableKafka
@EmbeddedKafka(partitions = 1,
		topics = {"my-topic" },
		brokerProperties = {
				"listeners=PLAINTEXT://localhost:12345",
				"port=12345"})
public class KafkaProcessorApplicationTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	public void contextLoads() {
	}

}
