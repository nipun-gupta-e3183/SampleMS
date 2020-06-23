package com.freshworks.boot.samples.sqs_processor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("WeakerAccess")
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureTestDatabase
public class SqsProcessorApplicationTests {

	@Test
	public void contextLoads() {
		assertTrue(true);
	}

}
