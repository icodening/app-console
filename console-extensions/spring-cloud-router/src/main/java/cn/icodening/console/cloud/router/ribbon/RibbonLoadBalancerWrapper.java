package cn.icodening.console.cloud.router.ribbon;

import cn.icodening.console.cloud.router.LoadBalancePreFilter;
import cn.icodening.console.cloud.router.RouterFilterConfigSource;
import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

/**
 * @author icodening
 * @date 2021.07.16
 */
public class RibbonLoadBalancerWrapper extends BaseLoadBalancer implements ILoadBalancer {

    private static final BiPredicate<String, String> equalsFunction = String::equals;

    private static final BiPredicate<String, String> regexFunction = Pattern::matches;

    private static final BiFunction<HttpRequest, String, String> headerSourceGetter = (httpRequest, key) -> httpRequest.getHeaders().getFirst(key);

    private final ILoadBalancer originLoadBalancer;

    private final Map<String, LoadBalancePreFilter<Server>> namedLoadBalancePreFilter;

    private final RouterFilterConfigSource routerFilterConfigSource;

    public RibbonLoadBalancerWrapper(ILoadBalancer originLoadBalancer,
                                     IClientConfig clientConfig,
                                     RouterFilterConfigSource routerFilterConfigSource) {
        this(originLoadBalancer, Collections.emptyMap(), clientConfig, routerFilterConfigSource);
        initWithNiwsConfig(clientConfig);
    }

    public RibbonLoadBalancerWrapper(ILoadBalancer originLoadBalancer,
                                     Map<String, LoadBalancePreFilter<Server>> namedLoadBalancePreFilter,
                                     IClientConfig clientConfig,
                                     RouterFilterConfigSource routerFilterConfigSource) {
        this.originLoadBalancer = originLoadBalancer;
        this.namedLoadBalancePreFilter = namedLoadBalancePreFilter;
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
        for (RouterFilterConfigEntity config : configs) {
            if (config.getEnable() != null && !config.getEnable()) {
                continue;
            }
            //1.根据key源(header、query)从请求中查对应的key 如不存在则忽略
            boolean containsKey = request.getHeaders().containsKey(config.getKeyName());
            if (!containsKey) {
                continue;
            }
            String value = headerSourceGetter.apply(request, config.getKeyName());
            if (!StringUtils.hasText(value)) {
                continue;
            }
            //2.根据配置的匹配类型(正则匹配、精确匹配)做对应的比较 如比较失败则忽略
            String matchType = config.getMatchType();
            String expression = config.getExpression();
            if (!("equals".equals(matchType))) {
                if (!value.equalsIgnoreCase(expression)) {
                    continue;
                }
            } else {
                if (!Pattern.matches(expression, value)) {
                    continue;
                }
            }
            //3.根据配置的过滤类型选择对应的过滤器
            String filterType = config.getFilterType();
            LoadBalancePreFilter<Server> serverLoadBalancePreFilter = namedLoadBalancePreFilter.get(filterType);
            if (serverLoadBalancePreFilter != null) {
                servers = serverLoadBalancePreFilter.filter(servers, config.getSignatureKey() + "=" + config.getServerInstanceSignature());
            }
            if (servers == null) {
                servers = Collections.emptyList();
            }
        }
        return servers;
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
