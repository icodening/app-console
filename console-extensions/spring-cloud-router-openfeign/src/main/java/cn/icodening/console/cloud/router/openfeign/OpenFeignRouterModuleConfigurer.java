package cn.icodening.console.cloud.router.openfeign;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class OpenFeignRouterModuleConfigurer implements ModuleRegistryConfigurer {


    private static final String SPRING_CLOUD_CONFIG_CLASS_NAME = "org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration";
    private static final String SPRING_CLOUD_LOADBALANCER_CLIENT_CLASS_NAME = "org.springframework.cloud.client.loadbalancer.LoadBalancerClient";
    private static final String SPRING_CLOUD_FEIGN_CONFIGURATION_CLASS_NAME = "org.springframework.cloud.openfeign.FeignAutoConfiguration";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsCloudConfig = ClassUtil.exists(SPRING_CLOUD_CONFIG_CLASS_NAME);
        boolean existsCloudLoadBalancer = ClassUtil.exists(SPRING_CLOUD_LOADBALANCER_CLIENT_CLASS_NAME);
        boolean existsFeignAutoConfiguration = ClassUtil.exists(SPRING_CLOUD_FEIGN_CONFIGURATION_CLASS_NAME);
        if (existsCloudConfig && existsCloudLoadBalancer && existsFeignAutoConfiguration) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
