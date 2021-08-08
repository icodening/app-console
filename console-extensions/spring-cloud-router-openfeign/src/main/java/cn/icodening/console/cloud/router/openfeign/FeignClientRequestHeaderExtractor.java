package cn.icodening.console.cloud.router.openfeign;

import cn.icodening.console.cloud.router.common.KeySourceExtractor;
import feign.Request;

import java.util.Collection;

/**
 * @author icodening
 * @date 2021.07.19
 */
public class FeignClientRequestHeaderExtractor implements KeySourceExtractor<Request> {

    @Override
    public boolean contains(Request target, String name) {
        return target.headers().containsKey(name);
    }

    @Override
    public String getValue(Request target, String name) {
        Collection<String> values = target.headers().get(name);
        return values != null && values.size() > 0 ? values.iterator().next() : null;
    }
}
