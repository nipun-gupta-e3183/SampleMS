package com.freshworks.starter.sample.sqs_processor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("WeakerAccess")
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureTestDatabase
public class SqsProcessorApplicationTests {

	@Test
	public void contextLoads() {
	}

}
