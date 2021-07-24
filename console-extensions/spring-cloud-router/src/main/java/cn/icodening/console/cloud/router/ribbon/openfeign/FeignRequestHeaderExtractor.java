package cn.icodening.console.cloud.router.ribbon.openfeign;

import cn.icodening.console.cloud.router.KeySourceExtractor;
import feign.RequestTemplate;

import java.util.Collection;

/**
 * @author icodening
 * @date 2021.07.19
 */
public class FeignRequestHeaderExtractor implements KeySourceExtractor<RequestTemplate> {

    @Override
    public String getValue(RequestTemplate target, String name) {
        Collection<String> values = target.headers().get(name);
        return values != null && values.size() > 0 ? values.iterator().next() : null;
    }
}
