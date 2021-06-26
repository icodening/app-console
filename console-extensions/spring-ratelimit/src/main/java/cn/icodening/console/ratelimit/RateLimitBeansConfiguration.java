package cn.icodening.console.ratelimit;

import cn.icodening.console.ratelimit.support.LocalRateLimiter;
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
}
