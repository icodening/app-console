package cn.icodening.console.cloud.router.config;

import cn.icodening.console.cloud.router.ribbon.eureka.EurekaRibbonLoadBalancePreFilter;
import cn.icodening.console.cloud.router.ribbon.eureka.EurekaServerMetaFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.07.18
 */
@ConditionalOnClass(name = "com.netflix.niws.loadbalancer.DiscoveryEnabledServer")
@ConditionalOnMissingClass("com.alibaba.cloud.nacos.ribbon.NacosServer")
public class EurekaSupportAutoConfiguration {

    @Bean
    public EurekaRibbonLoadBalancePreFilter eurekaRibbonLoadBalancePreFilter() {
        return new EurekaRibbonLoadBalancePreFilter();
    }

    @Bean
    public EurekaServerMetaFilter eurekaServerMetaFilter() {
        return new EurekaServerMetaFilter();
    }
}
