package cn.icodening.console.cloud.router.common;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class OnNoRibbonDefaultCondition extends AnyNestedCondition {

    public OnNoRibbonDefaultCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.ribbon.enabled",
            havingValue = "false")
    static class RibbonNotEnabled {

    }

    @ConditionalOnMissingClass("org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient")
    static class RibbonLoadBalancerNotPresent {

    }

}
