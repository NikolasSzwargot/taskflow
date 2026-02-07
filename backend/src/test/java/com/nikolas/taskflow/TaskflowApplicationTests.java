package com.nikolas.taskflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Application Context Tests")
class TaskflowApplicationTests {

	@Test
	@DisplayName("Should load Spring application context successfully")
	void contextLoads() {
	}

}
