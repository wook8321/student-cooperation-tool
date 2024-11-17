package com.stool.studentcooperationtools;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:application.yml")
@SpringBootTest
public abstract class IntegrationTest {
}
