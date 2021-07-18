package cn.icodening.console.cloud.router.ribbon;

import cn.icodening.console.cloud.router.SpringCloudServiceRegistry;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.List;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class RibbonServiceRegistry implements SpringCloudServiceRegistry<Server> {

    private final SpringClientFactory springClientFactory;

    public RibbonServiceRegistry(SpringClientFactory springClientFactory) {
        this.springClientFactory = springClientFactory;
    }

    @Override
    public List<Server> getServiceInstances(String serviceId) {
        ILoadBalancer loadBalancer = springClientFactory.getLoadBalancer(serviceId);
        if (!(loadBalancer instanceof RibbonLoadBalancerWrapper)) {
            return loadBalancer.getReachableServers();
        }
        return ((RibbonLoadBalancerWrapper) loadBalancer).getOriginLoadBalancer().getReachableServers();
    }
}
