package cn.icodening.console.cloud.router;

import cn.icodening.console.cloud.router.common.filter.LoadBalancePreFilter;
import cn.icodening.console.cloud.router.config.EurekaSupportAutoConfiguration;
import cn.icodening.console.cloud.router.config.NacosSupportAutoConfiguration;
import cn.icodening.console.cloud.router.ribbon.HostServerFilter;
import cn.icodening.console.cloud.router.ribbon.RibbonRouterClientSpecification;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author icodening
 * @date 2021.07.17
 */
@ImportAutoConfiguration({NacosSupportAutoConfiguration.class, EurekaSupportAutoConfiguration.class})
@Conditional(OnHasRibbonCondition.class)
public class RouterAutoConfiguration {

    @Bean
    public RibbonRouterClientSpecification routerClientSpecification() {
        return new RibbonRouterClientSpecification();
    }

    @Bean
    public LoadBalancePreFilter<Server> hostServerFilter() {
        return new HostServerFilter();
    }
}
