package cn.icodening.console.cloud.router;

import java.util.List;

/**
 * @author icodening
 * @date 2021.07.17
 */
public interface SpringCloudServiceRegistry<T> {

    /**
     * 获取服务实例列表
     *
     * @param serviceId 服务id
     * @return 服务实例列表
     */
    List<T> getServiceInstances(String serviceId);

}
