package cn.icodening.console.cloud.router.loadbalancer;

import cn.icodening.console.cloud.router.common.RouterFilterConfigSource;
import cn.icodening.console.cloud.router.common.RouterFilterHelper;
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
//        for (RouterFilterConfigEntity config : configs) {
//            if (config.getEnable() != null && !config.getEnable()) {
//                continue;
//            }
//            String keySource = config.getKeySource();
//            KeySourceExtractor<HttpRequest> httpRequestKeySourceExtractor = httpExtractorMap.get(keySource);
//            //1.根据key源(header、query)从请求中查对应的key 如不存在则忽略
//            boolean containsKey = httpRequestKeySourceExtractor.contains(request, config.getKeyName());
//            if (!containsKey) {
//                continue;
//            }
//            //2.根据配置的匹配类型(正则匹配、精确匹配)做对应的比较 如比较失败则忽略
//            String matchType = config.getMatchType();
//            String value = httpRequestKeySourceExtractor.getValue(request, config.getKeyName());
//            if (!StringUtils.hasText(value)) {
//                continue;
//            }
//            String expression = config.getExpression();
//            ExpressionMatcher expressionMatcher = expressionMatcherMap.get(matchType);
//            if (!expressionMatcher.match(expression, value)) {
//                continue;
//            }
//
//            //3.根据配置的过滤类型选择对应的过滤器
//            String filterType = config.getFilterType();
//            LoadBalancePreFilter<ServiceInstance> serverLoadBalancePreFilter = namedLoadBalancePreFilter.get(filterType);
//            if (serverLoadBalancePreFilter != null) {
//                servers = serverLoadBalancePreFilter.filter(servers, config.getSignatureKey() + "=" + config.getServerInstanceSignature());
//            }
//            if (servers == null) {
//                servers = Collections.emptyList();
//            }
//        }
//        return servers;
    }

    private HttpRequest getOriginHttpRequest() {
        return ThreadContextUtil.get("request", HttpRequest.class);
    }

    @Override
    public String getServiceId() {
        return delegate.getServiceId();
    }
}
