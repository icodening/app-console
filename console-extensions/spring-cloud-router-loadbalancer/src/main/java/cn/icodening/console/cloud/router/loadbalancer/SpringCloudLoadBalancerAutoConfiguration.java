package cn.icodening.console.cloud.router.loadbalancer;

import cn.icodening.console.cloud.router.common.OnNoRibbonDefaultCondition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author icodening
 * @date 2021.08.08
 */
@Conditional(OnNoRibbonDefaultCondition.class)
public class SpringCloudLoadBalancerAutoConfiguration {

    @Bean
    public SpringCloudLoadBalancerClientSpecification springCloudLoadBalancerClientSpecification() {
        return new SpringCloudLoadBalancerClientSpecification();
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (ServiceInstanceListSupplier.class.isAssignableFrom(bean.getClass())) {
                    return new ServiceInstanceListLoadBalancePreFilterSupplier((ServiceInstanceListSupplier) bean);
                }
                return bean;
            }
        };
    }

    @Bean
    public SmartInitializingSingleton initializingSingleton(ApplicationContext applicationContext,
                                                            @Autowired(required = false) ServiceInstanceListSupplier serviceInstanceListSupplier) {
        return () -> {
            if (serviceInstanceListSupplier != null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(serviceInstanceListSupplier);
            }
        };
    }
}
