package cn.icodening.console.cloud.router.common;

import org.springframework.cloud.client.ServiceInstance;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class ServiceInstanceHostFilter extends AbstractServiceInstanceLoadBalancePreFilter<ServiceInstance> {

    @Override
    protected boolean isEligible(ServiceInstance server, String param) {
        String value = param.substring(param.indexOf("=") + 1);
        return server.getHost().equals(value);
    }

    @Override
    public String name() {
        return "host";
    }
}
