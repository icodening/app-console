package cn.icodening.console.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author icodening
 * @date 2021.06.08
 */
@Configuration
public class ExecutorPoolConfiguration {

    @Bean
    public ThreadPoolTaskExecutor pushExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("push-message-thread-");
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
