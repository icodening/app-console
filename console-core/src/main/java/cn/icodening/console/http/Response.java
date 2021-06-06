package cn.icodening.console.http;

/**
 * @author icodening
 * @date 2021.06.03
 */
public interface Response extends HttpMessage {

    int getCode();

    byte[] getData();
}
