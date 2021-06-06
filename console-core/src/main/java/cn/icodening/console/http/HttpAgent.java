package cn.icodening.console.http;

import java.io.IOException;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.06.03
 */
public interface HttpAgent {

    int DEFAULT_READ_TIMEOUT = 15000;

    default Response get(String url, Map<String, String> queryParam, HttpHeaders httpHeaders) throws IOException {
        Request get = Request.of(url, "GET", queryParam, httpHeaders);
        return exchange(get);
    }

    default Response get(String url, HttpHeaders httpHeaders) throws IOException {
        return get(url, null, httpHeaders);
    }

    default Response get(String url) throws IOException {
        return get(url, null, null);
    }

    default Response get(Request request) throws IOException {
        return get(request.getUri(), request.getQueryParams(), request.getHeaders());
    }

    default Response post(String url, Map<String, String> queryParam, HttpHeaders httpHeaders, byte[] body) throws IOException {
        Request post = Request.of(url, "POST", queryParam, httpHeaders, body);
        return exchange(post);
    }

    default Response post(String url, Map<String, String> queryParam, HttpHeaders httpHeaders) throws IOException {
        return post(url, queryParam, httpHeaders, null);
    }

    default Response post(String url, Map<String, String> queryParam) throws IOException {
        return post(url, queryParam, null, null);
    }

    default Response post(String url) throws IOException {
        return post(url, null, null, null);
    }

    default Response post(String url, HttpHeaders httpHeaders) throws IOException {
        return post(url, null, httpHeaders, null);
    }

    default Response exchange(Request request) throws IOException {
        return exchange(request, DEFAULT_READ_TIMEOUT);
    }

    Response exchange(Request request, int timeout) throws IOException;

}
