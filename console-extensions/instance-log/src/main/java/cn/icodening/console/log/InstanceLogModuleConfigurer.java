package cn.icodening.console.log;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class InstanceLogModuleConfigurer implements ModuleRegistryConfigurer {

    private static final String CONTAINER_PROVIDER_CLASSNAME = "javax.websocket.ContainerProvider";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean exists = ClassUtil.exists(CONTAINER_PROVIDER_CLASSNAME);
        if (exists) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
