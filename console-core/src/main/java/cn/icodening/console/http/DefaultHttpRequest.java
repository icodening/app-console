package cn.icodening.console.http;

import java.util.Map;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class DefaultHttpRequest implements Request {

    private String uri;

    private String method = "GET";

    private Map<String, String> queryParams;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private byte[] body = new byte[0];


    public DefaultHttpRequest() {
    }

    public DefaultHttpRequest(String uri) {
        this.uri = uri;
    }

    public DefaultHttpRequest(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    public DefaultHttpRequest(String uri, String method, Map<String, String> queryParams) {
        this.uri = uri;
        this.method = method;
        this.queryParams = queryParams;
    }

    public DefaultHttpRequest(String uri, String method, Map<String, String> queryParams, HttpHeaders httpHeaders) {
        this.uri = uri;
        this.method = method;
        this.queryParams = queryParams;
        this.httpHeaders = httpHeaders;
    }

    public DefaultHttpRequest(String uri, String method, Map<String, String> queryParams, HttpHeaders httpHeaders, byte[] body) {
        this.uri = uri;
        this.method = method;
        this.queryParams = queryParams;
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        this.httpHeaders = headers;
    }
}
