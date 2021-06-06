package cn.icodening.console.http;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class DefaultHttpResponse implements Response {

    private int code = 200;

    private HttpHeaders httpHeaders;

    private byte[] data;


    public DefaultHttpResponse() {
    }

    public DefaultHttpResponse(int code) {
        this.code = code;
    }

    public DefaultHttpResponse(int code, HttpHeaders httpHeaders) {
        this.code = code;
        this.httpHeaders = httpHeaders;
    }

    public DefaultHttpResponse(int code, byte[] data) {
        this.code = code;
        this.data = data;
    }

    public DefaultHttpResponse(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public DefaultHttpResponse(byte[] data) {
        this.data = data;
    }

    public DefaultHttpResponse(int code, HttpHeaders httpHeaders, byte[] data) {
        this.code = code;
        this.httpHeaders = httpHeaders;
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        this.httpHeaders = httpHeaders;
    }
}
