package cn.icodening.console.http;

/**
 * @author icodening
 * @date 2021.06.03
 */
public interface HttpMessage {

    HttpHeaders getHeaders();

    void setHeaders(HttpHeaders headers);
}
