package cn.icodening.console.cloud.router.common;

import cn.icodening.console.cloud.router.common.store.RouterConfigSource;
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

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final URI originalUri = request.getURI();
        String serviceName = originalUri.getHost();
        List<RouterConfigEntity> configs = routerConfigSource.getConfigs(serviceName);
        String targetService = HttpRequestRouterHelper.getTargetService(configs, request);
        HttpRequest newRequest = request;
        if (!serviceName.equalsIgnoreCase(targetService)) {
            String newUri = request.getURI().toString().replace(serviceName, targetService);
            newRequest = createHttpRequest(URI.create(newUri), request.getMethodValue(), request.getHeaders());
        }
        try {
            ThreadContextUtil.set("request", newRequest);
            return execution.execute(newRequest, body);
        } finally {
            ThreadContextUtil.remove();
        }
    }

    private HttpRequest createHttpRequest(URI uri, String method, HttpHeaders httpHeaders) {
        return new HttpRequest() {
            @Override
            public String getMethodValue() {
                return method;
            }

            @Override
            public URI getURI() {
                return uri;
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpHeaders;
            }
        };
    }
}
