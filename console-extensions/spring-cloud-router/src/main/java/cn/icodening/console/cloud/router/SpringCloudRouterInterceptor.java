package cn.icodening.console.cloud.router;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Spring Cloud
 * 通过在线程上下文中传递目标值
 *
 * @author icodening
 * @date 2021.07.16
 */
public class SpringCloudRouterInterceptor implements ClientHttpRequestInterceptor {

    @Resource
    private RouterConfigSource routerConfigSource;

    @Resource
    private RouterFilterConfigSource routerFilterConfigSource;

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
            String value = request.getHeaders().getFirst(key);
            if (value == null) {
                continue;
            }
            String expression = config.getExpression();
            if ("equals".equals(matchType)) {
                if (value.equals(expression)) {
                    targetService = config.getTargetService();
                    break;
                }
            } else if ("regex".equals(matchType)) {
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
