package cn.icodening.console.cloud.router.ribbon.openfeign;

import cn.icodening.console.cloud.router.KeySourceExtractor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.07.19
 */
public class FeignRequestQueryExtractor implements KeySourceExtractor<RequestTemplate> {

    @Override
    public String getValue(RequestTemplate target, String name) {
        Map<String, Collection<String>> queries = target.queries();
        Collection<String> values = queries.get(name);
        if (values != null && values.size() > 0) {
            String next = values.iterator().next();
            if (StringUtils.hasText(next)) {
                return next;
            }
        }
        return null;
    }
}
