package cn.icodening.console.ratelimit;

import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.ratelimit.supoort.LocalRateLimiter;
import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.06.09
 */
public class RateLimitBeansConfiguration {

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Bean
    public RateLimiter jvmRateLimiter() {
        return new LocalRateLimiter();
    }

    @Bean
    public ConsoleEventListener<ServerMessageReceivedEvent> rateLimitConfigReceivedEventListener() {
        return new RateLimitConfigReceivedEventListener();
    }

    @Bean
    public RateLimitContextRefreshEventListener rateLimitContextRefreshEventListener() {
        return new RateLimitContextRefreshEventListener();
    }

}
