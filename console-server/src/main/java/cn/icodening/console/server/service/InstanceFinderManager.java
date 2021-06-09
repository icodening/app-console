package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.InstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据不同维度获取实例列表
 * 分组、应用、实例标识 等...  可扩展
 *
 * @author icodening
 * @date 2021.06.08
 */
@Component
public class InstanceFinderManager {

    private Map<String, InstanceFinder> instanceFinderMap;

    @Autowired
    public void setInstanceFinderMap(Map<String, InstanceFinder> instanceFinderMap) {
        final ConcurrentHashMap<String, InstanceFinder> map = new ConcurrentHashMap<>(4);
        instanceFinderMap.forEach((beanName, instanceFinder) -> map.put(instanceFinder.classify(), instanceFinder));
        this.instanceFinderMap = map;
    }

    public List<InstanceEntity> find(String classify, String value) {
        final InstanceFinder instanceFinder = instanceFinderMap.get(classify);
        final List<InstanceEntity> instanceEntities = instanceFinder.find(value);
        if (instanceEntities != null) {
            return instanceEntities;
        }
        return Collections.emptyList();
    }
}
