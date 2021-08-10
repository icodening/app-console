package cn.icodening.console.cloud.router.common.filter;

import cn.icodening.console.cloud.router.common.ExpressionMatcher;
import cn.icodening.console.cloud.router.common.HttpRequestExtractor;
import cn.icodening.console.cloud.router.common.KeySourceExtractor;
import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.extension.ExtensionLoader;
import cn.icodening.console.util.CaseInsensitiveKeyMap;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author icodening
 * @date 2021.08.09
 */
public class RouterFilterHelper {

    private static final RouterFilterHelper INSTANCE = new RouterFilterHelper();

    private Map<String, HttpRequestExtractor> httpRequestExtractorMap = new CaseInsensitiveKeyMap<>(4);

    private Map<String, ExpressionMatcher> expressionMatcherMap = new CaseInsensitiveKeyMap<>(4);

    @Resource
    private Map<String, LoadBalancePreFilter> namedLoadbalancePreFilterMap;

    public static RouterFilterHelper getInstance() {
        return INSTANCE;
    }

    @PostConstruct
    private void initialization() {
        Map<String, HttpRequestExtractor> extractorMap = ExtensionLoader.getExtensionLoader(HttpRequestExtractor.class).getAllExtensionMap();
        Map<String, ExpressionMatcher> expressionMatcherMap = ExtensionLoader.getExtensionLoader(ExpressionMatcher.class).getAllExtensionMap();
        this.httpRequestExtractorMap.putAll(extractorMap);
        this.expressionMatcherMap.putAll(expressionMatcherMap);

        Map<String, LoadBalancePreFilter> loadBalancePreFilterMap = new CaseInsensitiveKeyMap<>(4);
        Collection<LoadBalancePreFilter> values = this.namedLoadbalancePreFilterMap.values();
        for (LoadBalancePreFilter value : values) {
            loadBalancePreFilterMap.put(value.name(), value);
        }
        this.namedLoadbalancePreFilterMap.clear();
        this.namedLoadbalancePreFilterMap = loadBalancePreFilterMap;
    }

    private RouterFilterHelper() {
    }

    public static <T> List<T> filterServers(HttpRequest request, List<RouterFilterConfigEntity> configs, List<T> originServerList) {
        return INSTANCE.doFilterServers(request, configs, originServerList);
    }

    private <T> List<T> doFilterServers(HttpRequest request, List<RouterFilterConfigEntity> configs, List<T> originServerList) {
        List<T> servers = new ArrayList<>(originServerList);
        for (RouterFilterConfigEntity config : configs) {
            if (config.getEnable() != null && !config.getEnable()) {
                continue;
            }
            String keySource = config.getKeySource();
            KeySourceExtractor<HttpRequest> httpRequestKeySourceExtractor = httpRequestExtractorMap.get(keySource);
            //1.根据key源(header、query)从请求中查对应的key 如不存在则忽略
            boolean containsKey = httpRequestKeySourceExtractor.contains(request, config.getKeyName());
            if (!containsKey) {
                continue;
            }
            //2.根据配置的匹配类型(正则匹配、精确匹配)做对应的比较 如比较失败则忽略
            String matchType = config.getMatchType();
            String value = httpRequestKeySourceExtractor.getValue(request, config.getKeyName());
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String expression = config.getExpression();
            ExpressionMatcher expressionMatcher = expressionMatcherMap.get(matchType);
            if (!expressionMatcher.match(expression, value)) {
                continue;
            }

            //3.根据配置的过滤类型选择对应的过滤器
            String filterType = config.getFilterType();
            LoadBalancePreFilter<T> serverLoadBalancePreFilter = namedLoadbalancePreFilterMap.get(filterType);
            if (serverLoadBalancePreFilter != null) {
                servers = serverLoadBalancePreFilter.filter(servers, config.getSignatureKey() + "=" + config.getServerInstanceSignature());
            }
            if (servers == null) {
                servers = Collections.emptyList();
            }
        }
        return servers;
    }

}
