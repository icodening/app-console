package cn.icodening.console.cloud.router.ribbon.nacos;

import cn.icodening.console.cloud.router.ribbon.AbstractRibbonLoadBalancePreFilter;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Nacos实现适配, 同集群优先
 * 若需要热负载，则应避免使用{@link com.alibaba.cloud.nacos.ribbon.NacosRule}
 * <p>
 *
 * @author icodening
 * @date 2021.07.17
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NacosSameClusterRibbonLoadBalancePreFilter extends AbstractRibbonLoadBalancePreFilter<NacosServer> {

    public static final String NAME = "sameCluster";

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    protected boolean isEligible(NacosServer server, String param) {
        String clusterName = nacosDiscoveryProperties.getClusterName();
        return StringUtils.hasText(clusterName)
                && clusterName.equals(server.getInstance().getClusterName());
    }

    @Override
    public List<NacosServer> filter(List<NacosServer> originServerList, String param) {
        List<NacosServer> sameClusterInstances = super.filter(originServerList, param);
        if (!sameClusterInstances.isEmpty()) {
            return sameClusterInstances;
        }
        return originServerList;
    }

    @Override
    public String name() {
        return NAME;
    }
}
