package com.freshworks.boot.samples.kafka_processor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("WeakerAccess")
@SpringBootTest
@AutoConfigureTestDatabase
@EnableKafka
@EmbeddedKafka(partitions = 1,
		topics = {"my-topic" },
		brokerProperties = {
				"listeners=PLAINTEXT://localhost:12345",
				"port=12345"})
// @DirtiesContext makes sure embedded kafka is cleanly shutdown at the end of every test. Very inefficient though :(
// Please refer https://github.com/spring-projects/spring-kafka/issues/194
@DirtiesContext
public class KafkaProcessorApplicationTests {

	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	public void contextLoads() {
		assertThat(true).isTrue();
	}

}
