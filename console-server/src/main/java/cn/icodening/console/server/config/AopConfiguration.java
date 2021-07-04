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
    public DefaultPointcutAdvisor jpaSaveMethodBeforePointcutAdvisor(BaseRepositoryPointcut repositoryPointcut) {
        return new DefaultPointcutAdvisor(repositoryPointcut, new ModifyEntityAdvice());
    }

    @Bean
    public ConfigurableScopeEntityAdvice configurableScopeEntityAdvice() {
        return new ConfigurableScopeEntityAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor jpaSaveMethodAfterPointcutAdvisor(BaseRepositoryPointcut repositoryPointcut
            , ConfigurableScopeEntityAdvice configurableScopeEntityAdvice) {
        return new DefaultPointcutAdvisor(repositoryPointcut, configurableScopeEntityAdvice);
    }

    @Bean
    public BaseRepositoryPointcut repositoryPointcut() {
        return new BaseRepositoryPointcut();
    }


    @Bean
    public DeleteConfigurableScopeEntityAdvice deleteConfigurableScopeEntityAdvice() {
        return new DeleteConfigurableScopeEntityAdvice();
    }

    @Bean
    public DeleteRepositoryPointcut deleteRepositoryPointcut() {
        return new DeleteRepositoryPointcut();
    }

    @Bean
    public DefaultPointcutAdvisor jpaDeleteMethodAfterPointcutAdvisor(DeleteRepositoryPointcut deleteRepositoryPointcut
            , DeleteConfigurableScopeEntityAdvice deleteConfigurableScopeEntityAdvice) {
        return new DefaultPointcutAdvisor(deleteRepositoryPointcut, deleteConfigurableScopeEntityAdvice);
    }

}
