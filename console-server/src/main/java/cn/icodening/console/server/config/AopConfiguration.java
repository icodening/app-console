package cn.icodening.console.server.config;

import cn.icodening.console.server.aop.*;
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
    public IServicePointcut servicePointcut() {
        return new IServicePointcut();
    }

    @Bean
    public DefaultPointcutAdvisor jpaSaveMethodBeforePointcutAdvisor() {
        return new DefaultPointcutAdvisor(new SaveRepositoryPointcut(), new ModifyEntityAdvice());
    }

    @Bean
    public SaveConfigurableScopeEntityAdvice saveConfigurableScopeEntityAdvice() {
        return new SaveConfigurableScopeEntityAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor serviceSaveMethodAfterPointcutAdvisor(SaveConfigurableScopeEntityAdvice configurableScopeEntityAdvice) {
        return new DefaultPointcutAdvisor(new IServicePointcut(), configurableScopeEntityAdvice);
    }

    @Bean
    public DeleteConfigurableScopeEntityAdvice deleteConfigurableScopeEntityAdvice() {
        return new DeleteConfigurableScopeEntityAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor jpaDeleteMethodAfterPointcutAdvisor(DeleteConfigurableScopeEntityAdvice deleteConfigurableScopeEntityAdvice) {
        return new DefaultPointcutAdvisor(new DeleteRepositoryPointcut(), deleteConfigurableScopeEntityAdvice);
    }

}
