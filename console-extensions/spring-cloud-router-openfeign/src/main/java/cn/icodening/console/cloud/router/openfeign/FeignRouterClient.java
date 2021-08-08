package cn.icodening.console.cloud.router.openfeign;

import cn.icodening.console.cloud.router.common.*;
import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.CaseInsensitiveKeyMap;
import cn.icodening.console.util.ThreadContextUtil;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class FeignRouterClient implements Client {


    private final Map<String, KeySourceExtractor<Request>> feignExtractor = new CaseInsensitiveKeyMap<>();
    private final Map<String, ExpressionMatcher> expressionMatcherMap = new CaseInsensitiveKeyMap<>();

    @Resource
    private RouterConfigSource routerConfigSource;

    private final Client delegate;

    public FeignRouterClient(Client delegate) {
        this.delegate = delegate;
    }

    @PostConstruct
    public void initialization() {
        feignExtractor.putIfAbsent("header", new FeignClientRequestHeaderExtractor());
        feignExtractor.putIfAbsent("query", new FeignClientRequestQueryExtractor());

        expressionMatcherMap.putIfAbsent("regex", new ExpressionRegexMatcher());
        expressionMatcherMap.putIfAbsent("equals", new ExpressionEqualsMatcher());
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        Request routerRequest = checkRouteIfNecessary(request);
        HttpRequest httpRequest = buildHttpRequest(routerRequest);
        //FIXME 想办法回调移除上下文对象
        ThreadContextUtil.set("request", httpRequest);
        return delegate.execute(routerRequest, options);
    }

    private HttpRequest buildHttpRequest(Request request) {
        Map<String, Collection<String>> headers = request.headers();
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach((k, values) -> httpHeaders.addAll(k, new ArrayList<>(values)));
        String methodValue = request.method();
        URI uri = URI.create(request.url());
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

    private Request checkRouteIfNecessary(Request request) {
        String url = request.url();
        URI uri = URI.create(url);
        String serviceName = uri.getHost();
        List<RouterConfigEntity> configs = routerConfigSource.getConfigs(serviceName);
        String targetService = serviceName;
        //FIXME 按照优先级排序
        Map<String, Collection<String>> headers = request.headers();
        for (RouterConfigEntity config : configs) {
            String keySource = config.getKeySource();
            String key = config.getKeyName();
            if (!headers.containsKey(key)) {
                continue;
            }
            KeySourceExtractor<Request> requestTemplateKeySourceExtractor = feignExtractor.get(keySource);
            String value = requestTemplateKeySourceExtractor.getValue(request, config.getKeyName());
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
            return Request.create(request.method(), newUrl, request.headers(), request.body(), request.charset());
        }
        return request;
    }
}
