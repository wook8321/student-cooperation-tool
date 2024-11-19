package com.stool.studentcooperationtools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:application.yml")
@SpringBootTest(classes = StudentCooperationToolsApplication.class)
class StudentCooperationToolsApplicationTests {
    @Test
    void contextLoads() {
    }
}
