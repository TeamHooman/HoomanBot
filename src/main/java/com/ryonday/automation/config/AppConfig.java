package com.ryonday.automation.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableConfigurationProperties
public class AppConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize( 10 );
        executor.setQueueCapacity( 10 );
        executor.setThreadNamePrefix("thread");
        executor.setThreadGroupName("automation-group");
        executor.afterPropertiesSet();
        return executor;
    }

    @Bean(name = "commandQueue")
    public BlockingQueue<String> commandQueue() {
        return new LinkedBlockingQueue<>();
    }
}
