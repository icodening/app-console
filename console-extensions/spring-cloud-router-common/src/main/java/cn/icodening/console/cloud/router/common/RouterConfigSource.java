package cn.icodening.console.cloud.router.common;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.util.CaseInsensitiveKeyMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由配置缓存
 *
 * @author icodening
 * @date 2021.07.16
 */
public class RouterConfigSource {

    private final Map<String, Set<RouterConfigEntity>> route = new CaseInsensitiveKeyMap<>(new ConcurrentHashMap<>());

    public List<RouterConfigEntity> getConfigs(String targetServiceId) {
        Set<RouterConfigEntity> set = route.getOrDefault(targetServiceId, Collections.emptySet());
        return new ArrayList<>(set);
    }

    public void setConfigs(String name, List<RouterConfigEntity> configs) {
        if (configs == null) {
            return;
        }
        for (RouterConfigEntity config : configs) {
            setConfig(name, config);
        }
    }

    public void setConfig(String name, RouterConfigEntity config) {
        Set<RouterConfigEntity> routerFilterConfigEntities = route.get(name);
        if (routerFilterConfigEntities == null) {
            route.putIfAbsent(name, Collections.synchronizedSet(new HashSet<>()));
            routerFilterConfigEntities = route.get(name);
        }
        routerFilterConfigEntities.removeIf(contain -> config.getId().equals(contain.getId()));
        routerFilterConfigEntities.add(config);
    }
}
