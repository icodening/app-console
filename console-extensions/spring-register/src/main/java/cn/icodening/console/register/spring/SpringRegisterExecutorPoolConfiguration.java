package cn.icodening.console.register.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author icodening
 * @date 2021.07.03
 */
public class SpringRegisterExecutorPoolConfiguration {

    @Bean
    public ThreadPoolTaskExecutor dispatcherExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("EventDispatcher-Thread");
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
