package com.Project.QuickHost.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("sentimentExecutor") public Executor sentimentExecutor() {
        var ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(4); ex.setMaxPoolSize(16); ex.setQueueCapacity(500);
        ex.setThreadNamePrefix("sentiment-"); ex.initialize();
        return ex;
    }
    @Bean("graphExecutor") public Executor graphExecutor() {
        var ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(8); ex.setMaxPoolSize(32); ex.setQueueCapacity(1000);
        ex.setThreadNamePrefix("graph-"); ex.initialize();
        return ex;
    }
}
