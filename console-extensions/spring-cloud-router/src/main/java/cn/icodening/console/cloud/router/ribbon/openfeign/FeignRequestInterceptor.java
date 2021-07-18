package cn.icodening.console.cloud.router.ribbon.openfeign;

import cn.icodening.console.cloud.router.RouterConfigSource;
import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * @author icodening
 * @date 2021.07.18
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    //FIXME duplicated code
    private static final BiFunction<RequestTemplate, String, String> headerSourceGetter = (httpRequest, key) -> {
        Collection<String> strings = httpRequest.headers().get(key);
        if (strings != null && strings.size() > 0) {
            Iterator<String> iterator = strings.iterator();
            return iterator.next();
        }
        return null;
    };

    private static final BiFunction<RequestTemplate, String, String> querySourceGetter = (httpRequest, key) -> {
        Map<String, Collection<String>> queries = httpRequest.queries();
        Collection<String> values = queries.get(key);
        if (values != null && values.size() > 0) {
            String next = values.iterator().next();
            if (StringUtils.hasText(next)) {
                return next;
            }
        }
        return null;
    };

    @Resource
    private RouterConfigSource routerConfigSource;

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
            String matchType = config.getMatchType();
            String key = config.getKeyName();
            if (!headers.containsKey(key)) {
                continue;
            }
            String value = null;
            if ("HEADER".equalsIgnoreCase(keySource)) {
                value = headerSourceGetter.apply(template, config.getKeyName());
            } else if ("QUERY".equalsIgnoreCase(keySource)) {
                value = querySourceGetter.apply(template, config.getKeyName());
            }
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String expression = config.getExpression();
            if ("equals".equalsIgnoreCase(matchType)) {
                if (value.equals(expression)) {
                    targetService = config.getTargetService();
                    break;
                }
            } else if ("regex".equalsIgnoreCase(matchType)) {
                if (Pattern.matches(expression, value)) {
                    targetService = config.getTargetService();
                    break;
                }
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
