package cn.icodening.console.cloud.router;

import cn.icodening.console.cloud.router.common.LoadBalancePreFilter;
import cn.icodening.console.cloud.router.common.RouterFilterConfigSource;
import cn.icodening.console.cloud.router.ribbon.RibbonLoadBalancerWrapper;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class RouterBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (ILoadBalancer.class.isAssignableFrom(bean.getClass())
                && !RibbonLoadBalancerWrapper.class.isAssignableFrom(bean.getClass())) {
            List<LoadBalancePreFilter> serverLoadBalancePreFilters = getBeansIgnoreException(LoadBalancePreFilter.class);
            IClientConfig clientConfig = getBeanIgnoreException(IClientConfig.class);
            RouterFilterConfigSource routerFilterConfigSource = getBeanIgnoreException(RouterFilterConfigSource.class);
            IRule rule = applicationContext.getBean(IRule.class);
            RibbonLoadBalancerWrapper ribbonLoadBalancerWrapper;
            if (serverLoadBalancePreFilters == null || serverLoadBalancePreFilters.isEmpty()) {
                ribbonLoadBalancerWrapper = new RibbonLoadBalancerWrapper((ILoadBalancer) bean, clientConfig, routerFilterConfigSource);
            } else {
                ConcurrentHashMap<String, LoadBalancePreFilter<Server>> map = new ConcurrentHashMap<>();
                for (LoadBalancePreFilter loadBalancePreFilter : serverLoadBalancePreFilters) {
                    map.put(loadBalancePreFilter.name(), loadBalancePreFilter);
                }
                ribbonLoadBalancerWrapper = new RibbonLoadBalancerWrapper((ILoadBalancer) bean, map, clientConfig, routerFilterConfigSource);
            }
            if (BaseLoadBalancer.class.isAssignableFrom(bean.getClass())) {
                ((BaseLoadBalancer) bean).getRule().setLoadBalancer(ribbonLoadBalancerWrapper);
            }
            rule.setLoadBalancer(ribbonLoadBalancerWrapper);
            return ribbonLoadBalancerWrapper;
        }
        return bean;
    }

    private <T> T getBeanIgnoreException(Class<T> type) {
        try {
            return applicationContext.getBean(type);
        } catch (BeansException e) {
            return null;
        }
    }

    private <T> List<T> getBeansIgnoreException(Class<T> type) {
        try {
            ApplicationContext currentContext = applicationContext;
            Map<String, T> beansMap = new HashMap<>();
            while (currentContext != null) {
                Map<String, T> beansOfType = currentContext.getBeansOfType(type);
                beansMap.putAll(beansOfType);
                currentContext = currentContext.getParent();
            }
            return beansMap.values().stream().collect(Collectors.toList());
        } catch (BeansException e) {
            return null;
        }
    }

    private <T> Map<String, T> getBeansMapIgnoreException(Class<T> type) {
        try {
            return applicationContext.getBeansOfType(type);
        } catch (BeansException e) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
