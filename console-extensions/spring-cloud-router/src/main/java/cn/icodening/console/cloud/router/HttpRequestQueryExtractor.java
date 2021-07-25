
package cn.icodening.console.cloud.router;

import org.springframework.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.07.24
 */
public class HttpRequestQueryExtractor implements KeySourceExtractor<HttpRequest> {

    @Override
    public boolean contains(HttpRequest target, String name) {
        if (target == null
                || name == null) {
            return false;
        }
        String query = target.getURI().getQuery();
        Map<String, String> map = queryStringToMap(query);
        return map.containsKey(name);
    }

    @Override
    public String getValue(HttpRequest target, String name) {
        if (target == null
                || name == null) {
            return null;
        }
        String query = target.getURI().getQuery();
        Map<String, String> map = queryStringToMap(query);
        return map.get(name);
    }

    private Map<String, String> queryStringToMap(String queryString) {
        Map<String, String> map = new HashMap<>(12);
        String[] split = queryString.split("&");
        for (String kv : split) {
            int index = kv.indexOf("=");
            if (index == -1) {
                continue;
            }
            String k = kv.substring(0, index);
            String v = kv.substring(index + 1);
            map.put(k, v);
        }
        return map;
    }
}
