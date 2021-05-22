package cn.icodening.console.config;

import cn.icodening.console.AgentPath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.05.22
 */
public enum ConfigurationManager {
    /**
     * 实例
     */
    INSTANCE;

    private final ConcurrentHashMap<String, String> all = new ConcurrentHashMap<>(32);

    public static final String DEFAULT_CONFIG_PATH = AgentPath.INSTANCE.getPath() + "/config/config.properties";

    public String get(String key) {
        return all.get(key);
    }

    public void setAll(Map<String, String> map) {
        all.putAll(map);
    }

    public void set(String key, String value) {
        all.put(key, value);
    }

    public void remove(String key) {
        all.remove(key);
    }
}
