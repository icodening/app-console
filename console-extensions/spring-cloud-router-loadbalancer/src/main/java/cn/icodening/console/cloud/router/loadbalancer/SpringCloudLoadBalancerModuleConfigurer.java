package cn.icodening.console.cloud.router.loadbalancer;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.08.08
 */
public class SpringCloudLoadBalancerModuleConfigurer implements ModuleRegistryConfigurer {

    private static final String SERVICE_INSTANCE_SUPPLIER_CLASS = "org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsSupplierClass = ClassUtil.exists(SERVICE_INSTANCE_SUPPLIER_CLASS);
        if (existsSupplierClass) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
