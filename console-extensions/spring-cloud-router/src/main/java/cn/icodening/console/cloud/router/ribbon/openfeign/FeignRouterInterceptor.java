package cn.icodening.console.cloud.router.ribbon.openfeign;

import cn.icodening.console.cloud.router.*;
import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.CaseInsensitiveKeyMap;
import cn.icodening.console.util.ThreadContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.07.18
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class FeignRouterInterceptor implements RequestInterceptor {

    private final Map<String, KeySourceExtractor<RequestTemplate>> feignExtractor = new CaseInsensitiveKeyMap<>();
    private final Map<String, ExpressionMatcher> expressionMatcherMap = new CaseInsensitiveKeyMap<>();

    @Resource
    private RouterConfigSource routerConfigSource;

    @PostConstruct
    public void initialization() {
        feignExtractor.putIfAbsent("header", new FeignRequestHeaderExtractor());
        feignExtractor.putIfAbsent("query", new FeignRequestQueryExtractor());

        expressionMatcherMap.putIfAbsent("regex", new ExpressionRegexMatcher());
        expressionMatcherMap.putIfAbsent("equals", new ExpressionEqualsMatcher());
    }

    @Override
    public void apply(RequestTemplate template) {
        checkRouteIfNecessary(template);
        HttpRequest httpRequest = buildHttpRequest(template);
        //FIXME 想办法回调移除上下文对象
        ThreadContextUtil.set("request", httpRequest);
    }

    private void checkRouteIfNecessary(RequestTemplate template) {
        String serviceName = template.feignTarget().name();
        List<RouterConfigEntity> configs = routerConfigSource.getConfigs(serviceName);
        String targetService = serviceName;
        String url = template.feignTarget().url() + template.url();
        //FIXME 按照优先级排序
        Map<String, Collection<String>> headers = template.headers();
        for (RouterConfigEntity config : configs) {
            String keySource = config.getKeySource();
            String key = config.getKeyName();
            if (!headers.containsKey(key)) {
                continue;
            }
            KeySourceExtractor<RequestTemplate> requestTemplateKeySourceExtractor = feignExtractor.get(keySource);
            String value = requestTemplateKeySourceExtractor.getValue(template, config.getKeyName());
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String expression = config.getExpression();
            String matchType = config.getMatchType();
            ExpressionMatcher expressionMatcher = expressionMatcherMap.get(matchType);
            if (expressionMatcher.match(expression, value)) {
                targetService = config.getTargetService();
                break;
            }
        }
        String newUrl = url.replace(serviceName, targetService);
        if (!newUrl.equalsIgnoreCase(url)) {
            template.target(newUrl);
        }
    }

    private HttpRequest buildHttpRequest(RequestTemplate template) {
        Map<String, Collection<String>> headers = template.headers();
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach((k, values) -> httpHeaders.addAll(k, new ArrayList<>(values)));
        String methodValue = template.method();
        String url = template.feignTarget().url() + template.url();
        URI uri = URI.create(url);
        return new HttpRequest() {

            @Override
            public HttpHeaders getHeaders() {
                return httpHeaders;
            }

            @Override
            public String getMethodValue() {
                return methodValue;
            }

            @Override
            public URI getURI() {
                return uri;
            }
        };
    }
}
