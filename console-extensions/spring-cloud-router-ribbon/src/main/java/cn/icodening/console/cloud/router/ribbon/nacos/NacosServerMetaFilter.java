package cn.icodening.console.cloud.router.ribbon.nacos;

import cn.icodening.console.cloud.router.ribbon.AbstractRibbonLoadBalancePreFilter;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import org.springframework.util.StringUtils;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class NacosServerMetaFilter extends AbstractRibbonLoadBalancePreFilter<NacosServer> {

    public static final String NAME = "metadata";

    @Override
    protected boolean isEligible(NacosServer server, String param) {
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
        String meta = server.getMetadata().get(key);
        return value.equals(meta);
    }

    @Override
    public String name() {
        return NAME;
    }
}
