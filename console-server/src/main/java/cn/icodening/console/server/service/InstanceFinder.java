package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.InstanceEntity;

import java.util.List;

/**
 * 实例查询器，抽象该接口用于扩展更高维度的实例查询。instance -> application -> group ......
 *
 * @author icodening
 * @date 2021.06.08
 */
public interface InstanceFinder {

    /**
     * 查询器的类型
     *
     * @return 查询器类型
     */
    String classify();

    /**
     * 根据入参查询实例列表
     *
     * @param name 根据classify的不同，参数名的含义也不同。当name为应用名时，表示查询该应用下的所有实例列表。当name为实例唯一标识identity时，表示查询唯一实例
     * @return 实例列表
     */
    List<InstanceEntity> find(String name);
}
