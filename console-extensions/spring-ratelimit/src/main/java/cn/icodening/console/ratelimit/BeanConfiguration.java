package cn.icodening.console.ratelimit;

import cn.icodening.console.ratelimit.supoort.JVMRateLimiter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.06.09
 */
public class BeanConfiguration {

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Bean
    public RateLimiter jvmRateLimiter() {
        return new JVMRateLimiter();
    }

    @Bean
    public ApplicationListener receivedEventListener() {
        return new RateLimitConfigReceivedEventListener();
    }
}
