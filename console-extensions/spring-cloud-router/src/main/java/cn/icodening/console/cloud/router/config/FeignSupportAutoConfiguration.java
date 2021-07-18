package cn.icodening.console.cloud.router.config;

import cn.icodening.console.cloud.router.ribbon.openfeign.FeignRequestInterceptor;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author icodening
 * @date 2021.07.18
 */
@AutoConfigureAfter(FeignClientsConfiguration.class)
@Import(FeignClientsConfiguration.class)
@ConditionalOnClass(Feign.Builder.class)
public class FeignSupportAutoConfiguration {
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
