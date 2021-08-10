package cn.icodening.console.cloud.router.ribbon;

import cn.icodening.console.cloud.router.common.RouterFilterConfigSource;
import cn.icodening.console.cloud.router.common.RouterFilterHelper;
import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.16
 */
public class RibbonLoadBalancerWrapper extends BaseLoadBalancer implements ILoadBalancer {

    private final ILoadBalancer originLoadBalancer;

    private final RouterFilterConfigSource routerFilterConfigSource;

    public RibbonLoadBalancerWrapper(ILoadBalancer originLoadBalancer,
                                     IClientConfig clientConfig,
                                     RouterFilterConfigSource routerFilterConfigSource) {
        this.originLoadBalancer = originLoadBalancer;
        this.routerFilterConfigSource = routerFilterConfigSource;
        initWithNiwsConfig(clientConfig);
    }

    @Override
    public Server chooseServer(Object key) {
        return originLoadBalancer.chooseServer(key);
    }

    @Override
    public List<Server> getAllServers() {
        HttpRequest request = ThreadContextUtil.get("request", HttpRequest.class);
        List<Server> allServers = originLoadBalancer.getAllServers();
        if (request == null) {
            return allServers;
        }
        return filterServers(request, allServers);
    }

    private List<Server> filterServers(HttpRequest request, List<Server> originServerList) {
        List<RouterFilterConfigEntity> configs = routerFilterConfigSource.getConfigs(getName());
        List<Server> servers = new ArrayList<>(originServerList);
        return RouterFilterHelper.filterServers(request, configs, servers);
    }

    @Override
    public List<Server> getReachableServers() {
        HttpRequest request = ThreadContextUtil.get("request", HttpRequest.class);
        List<Server> reachableServers = originLoadBalancer.getReachableServers();
        if (request == null) {
            return originLoadBalancer.getReachableServers();
        }
        return filterServers(request, reachableServers);
    }

    public ILoadBalancer getOriginLoadBalancer() {
        return originLoadBalancer;
    }
}
