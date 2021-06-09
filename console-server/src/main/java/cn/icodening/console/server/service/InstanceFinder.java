package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.InstanceEntity;

import java.util.List;

/**
 * @author icodening
 * @date 2021.06.08
 */
public interface InstanceFinder {

    String classify();

    List<InstanceEntity> find(String name);
}
