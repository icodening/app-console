package cn.icodening.console.cloud.router;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Spring Cloud
 * 通过在线程上下文中传递目标值
 *
 * @author icodening
 * @date 2021.07.16
 */
public class SpringCloudRouterInterceptor implements ClientHttpRequestInterceptor {

    //FIXME duplicated code
    private static final BiFunction<HttpRequest, String, String> headerSourceGetter = (httpRequest, key) -> httpRequest.getHeaders().getFirst(key);

    private static final BiFunction<HttpRequest, String, String> querySourceGetter = (httpRequest, key) -> {
        String query = httpRequest.getURI().getQuery();
        Map<String, String> map = new HashMap<>();
        String[] split = query.split("&");
        for (String kv : split) {
            int index = kv.indexOf("=");
            if (index == -1) {
                continue;
            }
            String k = kv.substring(0, index);
            String v = kv.substring(index + 1);
            map.put(k, v);
        }
        return map.get(key);
    };

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
            String keySource = config.getKeySource();
            String matchType = config.getMatchType();
            String key = config.getKeyName();
            if (!request.getHeaders().containsKey(key)) {
                continue;
            }
            String value = null;
            if ("HEADER".equalsIgnoreCase(keySource)) {
                value = headerSourceGetter.apply(request, config.getKeyName());
            } else if ("QUERY".equalsIgnoreCase(keySource)) {
                value = querySourceGetter.apply(request, config.getKeyName());
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
