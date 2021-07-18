package cn.icodening.console.cloud.router.ribbon.eureka;

import cn.icodening.console.cloud.router.ribbon.AbstractRibbonLoadBalancePreFilter;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * Eureka 根据元数据过滤服务实例
 *
 * @author icodening
 * @date 2021.07.17
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EurekaServerMetaFilter extends AbstractRibbonLoadBalancePreFilter<DiscoveryEnabledServer> {

    @Override
    protected boolean isEligible(DiscoveryEnabledServer server, String param) {
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
        String meta = server.getInstanceInfo().getMetadata().get(key);
        return value.equals(meta);
    }

    @Override
    public String name() {
        return "metadata";
    }
}
