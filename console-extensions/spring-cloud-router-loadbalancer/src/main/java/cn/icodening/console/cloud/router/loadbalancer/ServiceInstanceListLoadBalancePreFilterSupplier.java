package cn.icodening.console.cloud.router.loadbalancer;

import cn.icodening.console.cloud.router.common.filter.RouterFilterHelper;
import cn.icodening.console.cloud.router.common.store.RouterFilterConfigSource;
import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpRequest;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.08.07
 */
public class ServiceInstanceListLoadBalancePreFilterSupplier implements ServiceInstanceListSupplier {

    @Resource
    private RouterFilterConfigSource routerFilterConfigSource;

    private final ServiceInstanceListSupplier delegate;

    public ServiceInstanceListLoadBalancePreFilterSupplier(ServiceInstanceListSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        HttpRequest httpRequest = getOriginHttpRequest();
        Flux<List<ServiceInstance>> fluxList = delegate.get();
        if (httpRequest == null) {
            return fluxList;
        }
        return fluxList.map(serviceInstances -> filterServers(httpRequest, serviceInstances));
    }

    private List<ServiceInstance> filterServers(HttpRequest request, List<ServiceInstance> originServerList) {
        //FIXME 获取被调用的服务id
        List<RouterFilterConfigEntity> configs = routerFilterConfigSource.getConfigs(getServiceId());
        List<ServiceInstance> servers = new ArrayList<>(originServerList);
        return RouterFilterHelper.filterServers(request, configs, servers);
    }

    private HttpRequest getOriginHttpRequest() {
        return ThreadContextUtil.get("request", HttpRequest.class);
    }

    @Override
    public String getServiceId() {
        return delegate.getServiceId();
    }
}
