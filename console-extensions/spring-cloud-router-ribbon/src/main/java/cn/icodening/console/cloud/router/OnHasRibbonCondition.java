package cn.icodening.console.cloud.router;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class OnHasRibbonCondition extends AnyNestedCondition {

    public OnHasRibbonCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.ribbon.enabled",
            havingValue = "true")
    static class RibbonEnabled {

    }

    @ConditionalOnClass(name = "org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient")
    static class RibbonLoadBalancerPresent {

    }

}
