package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.extension.ExtensionLoader;
import cn.icodening.console.monitor.sql.ProxyFilter;
import cn.icodening.console.monitor.sql.SQLFilterChain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.07.30
 */
public class ProxyFilterManager {

    private final static ProxyFilterManager INSTANCE = new ProxyFilterManager();

    private final Map<Class<?>, List<ProxyFilter>> proxyFilterMap = new ConcurrentHashMap<>();

    private ProxyFilterManager() {
        List<ProxyFilter> allFilters = ExtensionLoader.getExtensionLoader(ProxyFilter.class).getAllExtension();
        putMap(allFilters);
        Set<Class<?>> classes = proxyFilterMap.keySet();
        for (Class<?> clz : classes) {
            List<ProxyFilter> filters = proxyFilterMap.get(clz);
            Collections.sort(filters);
            proxyFilterMap.put(clz, filters);
        }
    }

    private void putMap(List<ProxyFilter> proxyFilters) {
        for (ProxyFilter proxyFilter : proxyFilters) {
            if (proxyFilter instanceof AbstractProxyFilter) {
                Class type = ((AbstractProxyFilter) proxyFilter).getType();
                List<ProxyFilter> filters = proxyFilterMap.get(type);
                if (filters == null) {
                    proxyFilterMap.putIfAbsent(type, new ArrayList<>());
                    filters = proxyFilterMap.get(type);
                }
                filters.add(proxyFilter);
            }
        }
    }

    public static <T> SQLFilterChain<T> getFilterChain(Class<T> type) {
        return INSTANCE.doGetFilterChain(type);
    }

    private <T> SQLFilterChain<T> doGetFilterChain(Class<T> type) {
        List<ProxyFilter> filters = proxyFilterMap.getOrDefault(type, Collections.emptyList());
        return new SQLFilterChain<>(filters);
    }
}
