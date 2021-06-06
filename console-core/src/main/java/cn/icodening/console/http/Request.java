package cn.icodening.console.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.06.03
 */
public interface Request extends HttpMessage {

    String getUri();

    void setUri(String uri);

    void setMethod(String method);

    default String getMethod() {
        return "GET";
    }

    void setQueryParams(Map<String, String> queryParam);

    Map<String, String> getQueryParams();

    void setBody(byte[] body);

    byte[] getBody();

    static Request of(String uri) {
        return of(uri, "GET", new HashMap<>(2), new HttpHeaders(), new byte[0]);
    }

    static Request of(String uri, String method) {
        return of(uri, method, new HashMap<>(2), new HttpHeaders(), new byte[0]);
    }

    static Request of(String uri, String method, Map<String, String> queryParams) {
        return of(uri, method, queryParams, new HttpHeaders(), new byte[0]);
    }

    static Request of(String uri, String method, Map<String, String> queryParams, HttpHeaders httpHeaders) {
        return of(uri, method, queryParams, httpHeaders, new byte[0]);
    }

    static Request of(String uri, String method, Map<String, String> queryParams, HttpHeaders httpHeaders, byte[] requestBody) {
        return new DefaultHttpRequest(uri, method, queryParams, httpHeaders, requestBody);
    }
}
