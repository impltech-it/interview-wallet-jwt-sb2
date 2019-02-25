package com.interview.task;

import com.interview.task.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Application entry point.
 */
@EnableConfigurationProperties(value = {
        UahCurrencyRateProperties.class,
        UsdCurrencyRateProperties.class,
        EurCurrencyRateProperties.class,
        JwtProperties.class,
        HeaderProperties.class})
@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }
}
