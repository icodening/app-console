package cn.icodening.console.cloud.router.common;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class ServiceInstanceMetaFilter extends AbstractServiceInstanceLoadBalancePreFilter<ServiceInstance> {

    @Override
    protected boolean isEligible(ServiceInstance server, String param) {
        Map<String, String> metadata = server.getMetadata();
        if (!StringUtils.hasText(param)
                || param.indexOf("=") < 1) {
            return false;
        }
        int index = param.indexOf("=");
        if (index == param.length() - 1) {
            return false;
        }
        String key = param.substring(0, index);
        String value = param.substring(index + 1);
        String meta = metadata.get(key);
        return value.equals(meta);
    }

    @Override
    public String name() {
        return "metadata";
    }
}
