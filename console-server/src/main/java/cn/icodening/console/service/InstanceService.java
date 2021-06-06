package cn.icodening.console.service;

import cn.icodening.console.entity.InstanceEntity;

/**
 * @author icodening
 * @date 2021.05.24
 */
public interface InstanceService extends IService<InstanceEntity> {

    void register(InstanceEntity instanceEntity);
}
