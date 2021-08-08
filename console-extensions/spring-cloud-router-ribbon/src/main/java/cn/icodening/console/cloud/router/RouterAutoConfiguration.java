package cn.icodening.console.cloud.router;

import cn.icodening.console.cloud.router.common.LoadBalancePreFilter;
import cn.icodening.console.cloud.router.config.EurekaSupportAutoConfiguration;
import cn.icodening.console.cloud.router.config.NacosSupportAutoConfiguration;
import cn.icodening.console.cloud.router.ribbon.HostServerFilter;
import cn.icodening.console.cloud.router.ribbon.RibbonRouterClientSpecification;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

//import cn.icodening.console.cloud.router.config.FeignSupportAutoConfiguration;

/**
 * @author icodening
 * @date 2021.07.17
 */
@ImportAutoConfiguration({NacosSupportAutoConfiguration.class, EurekaSupportAutoConfiguration.class})
//@Import({FeignSupportAutoConfiguration.class})
public class RouterAutoConfiguration {

    @Bean
    public RibbonRouterClientSpecification routerClientSpecification() {
        return new RibbonRouterClientSpecification();
    }

//    @Bean
//    public RouterConfigSource routerConfigSource() {
//        return new RouterConfigSource();
//    }
//
//    @Bean
//    public RouterFilterConfigSource routerFilterConfigSource() {
//        return new RouterFilterConfigSource();
//    }

    @Bean
    public LoadBalancePreFilter<Server> hostServerFilter() {
        return new HostServerFilter();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public SpringCloudRouterInterceptor springCloudRouterInterceptor() {
//        return new SpringCloudRouterInterceptor();
//    }

//    @Bean
//    public SmartInitializingSingleton restTemplateConfigurer(List<RestTemplate> restTemplates, SpringCloudRouterInterceptor springCloudInterceptor) {
//        return () -> {
//            for (RestTemplate restTemplate : restTemplates) {
//                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
//                interceptors.add(0, springCloudInterceptor);
//                restTemplate.setInterceptors(interceptors);
//            }
//        };
//    }
}
