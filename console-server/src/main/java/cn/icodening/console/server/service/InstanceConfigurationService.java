package cn.icodening.console.server.service;

import cn.icodening.console.common.entity.ConfigurationType;
import cn.icodening.console.common.entity.InstanceEntity;

import java.util.List;

/**
 * @author icodening
 * @date 2021.06.12
 */
public interface InstanceConfigurationService<T> extends ConfigurationType {

    List<T> findInstanceConfiguration(InstanceEntity instanceEntity);

}
