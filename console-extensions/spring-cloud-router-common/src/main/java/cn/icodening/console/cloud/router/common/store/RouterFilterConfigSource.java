package cn.icodening.console.cloud.router.common.store;

import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.util.CaseInsensitiveKeyMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由配置缓存
 *
 * @author icodening
 * @date 2021.07.16
 */
public class RouterFilterConfigSource {

    private final Map<String, Set<RouterFilterConfigEntity>> route = new CaseInsensitiveKeyMap<>(new ConcurrentHashMap<>());

    public List<RouterFilterConfigEntity> getConfigs(String targetServiceId) {
        Set<RouterFilterConfigEntity> set = route.getOrDefault(targetServiceId, Collections.emptySet());
        return new ArrayList<>(set);
    }

    public void setConfigs(String name, List<RouterFilterConfigEntity> configs) {
        if (configs == null) {
            return;
        }
        for (RouterFilterConfigEntity config : configs) {
            setConfig(name, config);
        }
    }

    public void setConfig(String name, RouterFilterConfigEntity config) {
        Set<RouterFilterConfigEntity> routerFilterConfigEntities = route.get(name);
        if (routerFilterConfigEntities == null) {
            route.putIfAbsent(name, Collections.synchronizedSet(new HashSet<>()));
            routerFilterConfigEntities = route.get(name);
        }
        routerFilterConfigEntities.removeIf(contain -> config.getId().equals(contain.getId()));
        routerFilterConfigEntities.add(config);
    }
}
