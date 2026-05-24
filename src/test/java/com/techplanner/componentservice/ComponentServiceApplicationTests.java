package com.techplanner.componentservice;

import com.techplanner.componentservice.config.RequestLoggingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ComponentServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RequestLoggingFilter requestLoggingFilter;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(requestLoggingFilter).isNotNull();
        assertThat(applicationContext.containsBean("requestLoggingFilter")).isTrue();
    }
}