package cn.icodening.console.cloud.router.ribbon;

import com.netflix.loadbalancer.Server;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class HostServerFilter extends AbstractRibbonLoadBalancePreFilter<Server> {

    public static final String NAME = "host";

    @Override
    protected boolean isEligible(Server server, String param) {
        String value = param.substring(param.indexOf("=") + 1);
        return server.getHost().equals(value);
    }

    @Override
    public String name() {
        return NAME;
    }
}
