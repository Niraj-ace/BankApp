package com.bankapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuration for task scheduling with a custom thread pool.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    /**
     * Defines a custom ThreadPoolTaskScheduler bean to handle scheduled tasks.
     */
    @Bean
    public ThreadPoolTaskScheduler customTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // allow up to 5 scheduled tasks at once
        scheduler.setThreadNamePrefix("SchedulerThread-");
        scheduler.initialize();
        return scheduler;
    }
}
