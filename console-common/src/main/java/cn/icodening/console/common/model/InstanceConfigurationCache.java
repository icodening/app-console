package cn.icodening.console.common.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.06.10
 */
public class InstanceConfigurationCache<T> {

    private static final Map<Class<?>, InstanceConfigurationCache<?>> CONFIGURATION_CACHE_MAP = new ConcurrentHashMap<>();

    private final Class<T> clz;

    private List<T> configs;

    private InstanceConfigurationCache(Class<T> clz) {
        this.clz = clz;
    }

    private Class<T> getClz() {
        return clz;
    }

    private List<T> getConfigs() {
        return configs;
    }

    private void setConfigs(List<T> configs) {
        this.configs = configs;
    }

    public static <T> List<T> getConfigs(Class<T> clz) {
        InstanceConfigurationCache<T> instanceConfigurationCache = getCache(clz);
        return instanceConfigurationCache.getConfigs();
    }

    public static <T> T getConfig(Class<T> clz) {
        InstanceConfigurationCache<T> instanceConfigurationCache = getCache(clz);
        return instanceConfigurationCache.getConfigs().get(0);
    }

    public static <T> void setConfigs(Class<T> clz, List<T> data) {
        InstanceConfigurationCache<T> instanceConfigurationCache = getCache(clz);
        if (data == null || data.isEmpty()) {
            instanceConfigurationCache.setConfigs(Collections.emptyList());
            return;
        }
        instanceConfigurationCache.setConfigs(data);
    }

    @SuppressWarnings("unchecked")
    private static <T> InstanceConfigurationCache<T> getCache(Class<T> clz) {
        InstanceConfigurationCache<?> instanceConfigurationCache = CONFIGURATION_CACHE_MAP.get(clz);
        if (instanceConfigurationCache == null) {
            CONFIGURATION_CACHE_MAP.putIfAbsent(clz, new InstanceConfigurationCache<>(clz));
            instanceConfigurationCache = CONFIGURATION_CACHE_MAP.get(clz);
        }
        return (InstanceConfigurationCache<T>) instanceConfigurationCache;
    }
}
