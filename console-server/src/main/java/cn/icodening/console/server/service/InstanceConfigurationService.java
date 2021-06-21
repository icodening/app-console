package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.ConfigurationType;
import cn.icodening.console.common.entity.InstanceEntity;

import java.util.List;

/**
 * @author icodening
 * @date 2021.06.12
 */
public interface InstanceConfigurationService<T> extends ConfigurationType {

    /**
     * 查询指定实例下的配置信息
     *
     * @param instanceEntity 实例
     * @return 实例配置
     */
    List<T> findInstanceConfiguration(InstanceEntity instanceEntity);

}
