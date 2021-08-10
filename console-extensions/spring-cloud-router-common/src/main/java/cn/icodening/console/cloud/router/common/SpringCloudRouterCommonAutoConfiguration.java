package cn.icodening.console.cloud.router.common;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.17
 */
@Import(SpringCloudRouterCommonAutoConfiguration.NoRibbonFilterConfiguration.class)
public class SpringCloudRouterCommonAutoConfiguration {

    @Bean
    public ExpressionMatcher expressionRegexMatcher() {
        return new ExpressionRegexMatcher();
    }

    @Bean
    public ExpressionMatcher expressionEqualsMatcher() {
        return new ExpressionEqualsMatcher();
    }

    @Bean
    public HttpRequestExtractor httpRequestHeaderExtractor() {
        return new HttpRequestHeaderExtractor();
    }

    @Bean
    public HttpRequestExtractor httpRequestQueryExtractor() {
        return new HttpRequestQueryExtractor();
    }

    @Bean
    public RouterFilterHelper routerFilterHelper() {
        return RouterFilterHelper.getInstance();
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
    @ConditionalOnMissingBean
    public SpringCloudRouterInterceptor springCloudRouterInterceptor() {
        return new SpringCloudRouterInterceptor();
    }

    @Bean
    public SmartInitializingSingleton restTemplateConfigurer(List<RestTemplate> restTemplates, SpringCloudRouterInterceptor springCloudInterceptor) {
        return () -> {
            for (RestTemplate restTemplate : restTemplates) {
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
                interceptors.add(0, springCloudInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        };
    }

    @Conditional(OnNoRibbonDefaultCondition.class)
    public static class NoRibbonFilterConfiguration {

        @Bean
        public ServiceInstanceHostFilter serviceInstanceHostFilter() {
            return new ServiceInstanceHostFilter();
        }

        @Bean
        public ServiceInstanceMetaFilter serviceInstanceMetaFilter() {
            return new ServiceInstanceMetaFilter();
        }
    }
}
