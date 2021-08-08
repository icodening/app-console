package cn.icodening.console.cloud.router.openfeign;

import feign.Client;
import feign.Feign;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.ApplicationContext;
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
    public BeanPostProcessor routeClientWrapper() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (Client.class.isAssignableFrom(bean.getClass())) {
                    return new FeignRouterClient((Client) bean);
                }
                return bean;
            }
        };
    }

    @Bean
    public SmartInitializingSingleton routeClientAutowired(ApplicationContext applicationContext) {
        return () -> {
            Client bean = applicationContext.getBean(Client.class);
            if (bean instanceof FeignRouterClient) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
            }
        };
    }

}
