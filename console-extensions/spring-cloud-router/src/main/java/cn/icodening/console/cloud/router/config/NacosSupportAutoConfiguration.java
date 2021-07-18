package cn.icodening.console.cloud.router.config;

import cn.icodening.console.cloud.router.ribbon.nacos.NacosSameClusterRibbonLoadBalancePreFilter;
import cn.icodening.console.cloud.router.ribbon.nacos.NacosServerMetaFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.07.18
 */
@ConditionalOnClass(name = "com.alibaba.cloud.nacos.ribbon.NacosServer")
@ConditionalOnMissingClass("com.netflix.niws.loadbalancer.DiscoveryEnabledServer")
public class NacosSupportAutoConfiguration {

    @Bean
    public NacosSameClusterRibbonLoadBalancePreFilter nacosRibbonLoadBalancePreFilter() {
        return new NacosSameClusterRibbonLoadBalancePreFilter();
    }

    @Bean
    public NacosServerMetaFilter nacosServerMetaFilter() {
        return new NacosServerMetaFilter();
    }
}
