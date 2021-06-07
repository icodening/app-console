package cn.icodening.console.server.config;

import cn.icodening.console.server.aop.ModifyEntityAdvice;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author icodening
 * @date 2021.06.07
 */
@Configuration
public class AopConfiguration {

    @Bean
    public DefaultPointcutAdvisor jpaSaveMethodPointcutAdvisor() {
        ModifyEntityAdvice modifyEntityAdvice = new ModifyEntityAdvice();
        return new DefaultPointcutAdvisor(modifyEntityAdvice.getPointcut(), modifyEntityAdvice);
    }
}
