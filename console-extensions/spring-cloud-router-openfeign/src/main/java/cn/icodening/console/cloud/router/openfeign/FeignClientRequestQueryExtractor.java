package cn.icodening.console.cloud.router.openfeign;

import cn.icodening.console.cloud.router.common.KeySourceExtractor;
import feign.Request;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.07.19
 */
public class FeignClientRequestQueryExtractor implements KeySourceExtractor<Request> {

    @Override
    public boolean contains(Request target, String name) {
        Map<String, String> queryMap = getQueryMap(target);
        return queryMap.containsKey(name);
    }

    @Override
    public String getValue(Request target, String name) {
        Map<String, String> queryMap = getQueryMap(target);
        return queryMap.get(name);
    }

    private Map<String, String> getQueryMap(Request request) {
        String url = request.url();
        String query = URI.create(url).getQuery();
        Map<String, String> ret = new HashMap<>();
        String[] queries = query.split("&");
        for (String kv : queries) {
            int index = kv.indexOf("=");
            if (index == -1) {
                continue;
            }
            String paramName = kv.substring(0, index);
            String paramValue = kv.substring(index + 1);
            ret.put(paramName, paramValue);
        }
        return ret;
    }
}
