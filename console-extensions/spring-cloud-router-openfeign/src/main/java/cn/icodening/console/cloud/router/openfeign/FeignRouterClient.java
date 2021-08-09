package cn.icodening.console.cloud.router.openfeign;

import cn.icodening.console.cloud.router.common.HttpRequestRouterHelper;
import cn.icodening.console.cloud.router.common.RouterConfigSource;
import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.ThreadContextUtil;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

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

    @Resource
    private RouterConfigSource routerConfigSource;

    private final Client delegate;

    public FeignRouterClient(Client delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        String url = request.url();
        String serviceName = URI.create(url).getHost();
        HttpRequest httpRequest = buildHttpRequest(request);
        String serviceId = httpRequest.getURI().getHost();
        List<RouterConfigEntity> configs = routerConfigSource.getConfigs(serviceId);
        String targetService = HttpRequestRouterHelper.getTargetService(configs, httpRequest);

        if (!serviceName.equalsIgnoreCase(targetService)) {
            String newUrl = url.replace(serviceName, targetService);
            request = Request.create(request.method(), newUrl, request.headers(), request.body(), request.charset());
        }
        //FIXME 想办法回调移除上下文对象
        ThreadContextUtil.set("request", httpRequest);
        return delegate.execute(request, options);
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
}
