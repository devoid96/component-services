package com.techplanner.componentservice.config;

import com.techplanner.compatibilitylib.analyzers.CompatibilityAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompatibilityAnalyzerConfig {

    @Bean
    public CompatibilityAnalyzer compatibilityAnalyzer() {
        return new CompatibilityAnalyzer();
    }
}