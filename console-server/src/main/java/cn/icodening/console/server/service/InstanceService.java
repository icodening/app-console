package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.InstanceEntity;

/**
 * @author icodening
 * @date 2021.05.24
 */
public interface InstanceService extends IService<InstanceEntity> {

    void register(InstanceEntity instanceEntity);

    void deregister(String identity);
}
