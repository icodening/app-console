package cn.icodening.console.util;

import cn.icodening.console.http.HttpAgent;
import cn.icodening.console.http.Request;
import cn.icodening.console.http.Response;
import cn.icodening.console.http.jdk.DefaultHttpAgent;

import java.io.IOException;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class HttpUtil {

    private static final HttpAgent AGENT = new DefaultHttpAgent();

    private HttpUtil() {
    }

    public static Response exchange(Request request) throws IOException {
        return AGENT.exchange(request, HttpAgent.DEFAULT_READ_TIMEOUT);
    }

    public static Response exchange(Request request, int timeout) throws IOException {
        return AGENT.exchange(request, timeout);
    }

}
