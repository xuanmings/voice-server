package com.mifa.cloud.voice.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池配置
 *
 * @author 宋烜明
 *
 */
@Configuration
public class AsyncConfig {
    /**
     * 自定义异步线程池
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Executor");
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        return executor;
    }
}
