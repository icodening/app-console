package cn.icodening.console.cloud.router.ribbon.eureka;

import cn.icodening.console.cloud.router.ribbon.AbstractRibbonLoadBalancePreFilter;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Eureka实现适配
 * 空实现
 *
 * @author icodening
 * @date 2021.07.17
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EurekaRibbonLoadBalancePreFilter extends AbstractRibbonLoadBalancePreFilter<DiscoveryEnabledServer> {

    @Override
    protected boolean isEligible(DiscoveryEnabledServer server, String param) {
        return true;
    }

    @Override
    public String name() {
        return "eureka";
    }
}
