package cn.icodening.console.cloud.router;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.CaseInsensitiveKeyMap;
import cn.icodening.console.util.ThreadContextUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Spring Cloud
 * 通过在线程上下文中传递目标值
 *
 * @author icodening
 * @date 2021.07.16
 */
public class SpringCloudRouterInterceptor implements ClientHttpRequestInterceptor {

    private final Map<String, KeySourceExtractor<HttpRequest>> httpExtractorMap = new CaseInsensitiveKeyMap<>();
    private final Map<String, ExpressionMatcher> expressionMatcherMap = new CaseInsensitiveKeyMap<>();

    @PostConstruct
    public void initialization() {
        httpExtractorMap.putIfAbsent("header", new HttpRequestHeaderExtractor());
        httpExtractorMap.putIfAbsent("query", new HttpRequestQueryExtractor());

        expressionMatcherMap.putIfAbsent("regex", new ExpressionRegexMatcher());
        expressionMatcherMap.putIfAbsent("equals", new ExpressionEqualsMatcher());
    }

    @Resource
    private RouterConfigSource routerConfigSource;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final URI originalUri = request.getURI();
        String serviceName = originalUri.getHost();
        List<RouterConfigEntity> configs = routerConfigSource.getConfigs(serviceName);
        String targetService = serviceName;
        //FIXME 按照优先级排序
        for (RouterConfigEntity config : configs) {
            if (config.getEnable() == null || !config.getEnable()) {
                continue;
            }
            config.getEnable();
            Boolean.valueOf(config.getEnable());
            String keySource = config.getKeySource();
            String matchType = config.getMatchType();
            String key = config.getKeyName();
            KeySourceExtractor<HttpRequest> httpRequestKeySourceExtractor = httpExtractorMap.get(keySource);
            boolean contains = httpRequestKeySourceExtractor.contains(request, key);
            if (!contains) {
                continue;
            }
            String value = httpRequestKeySourceExtractor.getValue(request, config.getKeyName());
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String expression = config.getExpression();
            ExpressionMatcher expressionMatcher = expressionMatcherMap.get(matchType);
            if (expressionMatcher.match(expression, value)) {
                targetService = config.getTargetService();
                break;
            }
        }
        HttpRequest newRequest = request;
        if (!serviceName.equalsIgnoreCase(targetService)) {
            String newUri = request.getURI().toString().replace(serviceName, targetService);
            newRequest = new HttpRequest() {
                @Override
                public String getMethodValue() {
                    return request.getMethodValue();
                }

                @Override
                public URI getURI() {
                    return URI.create(newUri);
                }

                @Override
                public HttpHeaders getHeaders() {
                    return request.getHeaders();
                }
            };
        }
        try {
            ThreadContextUtil.set("request", newRequest);
            return execution.execute(newRequest, body);
        } finally {
            ThreadContextUtil.remove();
        }
    }
}
