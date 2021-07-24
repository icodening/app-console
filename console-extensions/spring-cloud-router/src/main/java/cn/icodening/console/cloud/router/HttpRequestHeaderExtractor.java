package cn.icodening.console.cloud.router;

import org.springframework.http.HttpRequest;

/**
 * @author icodening
 * @date 2021.07.24
 */
public class HttpRequestHeaderExtractor implements KeySourceExtractor<HttpRequest> {

    @Override
    public String getValue(HttpRequest target, String name) {
        if (target == null
                || name == null) {
            return null;
        }
        return target.getHeaders().getFirst(name);
    }
}
