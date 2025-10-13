package com.bankapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class to enable async processing.
 * Defines a custom thread pool for @Async methods.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // Minimum threads
        executor.setMaxPoolSize(10);       // Maximum threads
        executor.setQueueCapacity(50);     // Queue capacity before creating new threads
        executor.setThreadNamePrefix("AsyncThread-"); 
        executor.initialize();
        return executor;
    }
}
