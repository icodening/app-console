package cn.icodening.console.cloud.router.common;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class SpringCloudRouterModuleConfigurer implements ModuleRegistryConfigurer {


    private static final String SPRING_CLOUD_CONFIG_CLASS_NAME = "org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration";
    private static final String SPRING_CLOUD_LOADBALANCER_CLIENT_CLASS_NAME = "org.springframework.cloud.client.loadbalancer.LoadBalancerClient";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsCloudConfig = ClassUtil.exists(SPRING_CLOUD_CONFIG_CLASS_NAME);
        boolean existsCloudLoadBalancer = ClassUtil.exists(SPRING_CLOUD_LOADBALANCER_CLIENT_CLASS_NAME);
        if (existsCloudConfig && existsCloudLoadBalancer) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
