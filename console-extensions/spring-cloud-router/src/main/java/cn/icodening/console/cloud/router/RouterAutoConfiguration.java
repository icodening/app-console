package cn.icodening.console.cloud.router;

import cn.icodening.console.cloud.router.config.EurekaSupportAutoConfiguration;
import cn.icodening.console.cloud.router.config.NacosSupportAutoConfiguration;
import cn.icodening.console.cloud.router.ribbon.HostServerFilter;
import cn.icodening.console.cloud.router.ribbon.RibbonRouterClientSpecification;
import cn.icodening.console.cloud.router.ribbon.RibbonServiceRegistry;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.17
 */
@ImportAutoConfiguration({NacosSupportAutoConfiguration.class, EurekaSupportAutoConfiguration.class})
public class RouterAutoConfiguration {

    @Bean
    public SpringCloudServiceRegistry<Server> springCloudServiceRegistry(SpringClientFactory springClientFactory) {
        return new RibbonServiceRegistry(springClientFactory);
    }

    @Bean
    public RibbonRouterClientSpecification routerClientSpecification() {
        return new RibbonRouterClientSpecification();
    }

    @Bean
    public RouterConfigSource routerConfigSource() {
        return new RouterConfigSource();
    }

    @Bean
    public RouterFilterConfigSource routerFilterConfigSource() {
        return new RouterFilterConfigSource();
    }

    @Bean
    public LoadBalancePreFilter<Server> hostServerFilter() {
        return new HostServerFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringCloudRouterInterceptor springCloudRouterInterceptor() {
        return new SpringCloudRouterInterceptor();
    }

    @Bean
    @ConditionalOnClass(LoadBalancerInterceptor.class)
    public RestTemplateCustomizer restTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor, SpringCloudRouterInterceptor springCloudRouterInterceptor) {
        return restTemplate -> {
            List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                    restTemplate.getInterceptors());
            list.add(0, springCloudRouterInterceptor);
            list.add(loadBalancerInterceptor);
            restTemplate.setInterceptors(list);
        };
    }
}
