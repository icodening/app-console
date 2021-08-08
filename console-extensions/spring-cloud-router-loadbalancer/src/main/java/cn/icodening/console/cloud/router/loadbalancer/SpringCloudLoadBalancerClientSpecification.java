package cn.icodening.console.cloud.router.loadbalancer;

import cn.icodening.console.cloud.router.common.SpringCloudRouterCommonAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class SpringCloudLoadBalancerClientSpecification extends LoadBalancerClientSpecification {

    @Override
    public String getName() {
        return "default.cn.icodening.console.cloud.router.loadbalancer.SpringCloudLoadBalancerClientSpecification";
    }

    @Override
    public Class<?>[] getConfiguration() {
        return new Class[]{SpringCloudLoadBalancerAutoConfiguration.class, SpringCloudRouterCommonAutoConfiguration.NoRibbonFilterConfiguration.class};
    }
}
