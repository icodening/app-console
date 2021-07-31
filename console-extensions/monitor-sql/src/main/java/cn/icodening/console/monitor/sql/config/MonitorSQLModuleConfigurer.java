package cn.icodening.console.monitor.sql.config;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;

/**
 * @author icodening
 * @date 2021.07.29
 */
public class MonitorSQLModuleConfigurer implements ModuleRegistryConfigurer {

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        moduleRegistry.registerCurrentModule();
    }
}
