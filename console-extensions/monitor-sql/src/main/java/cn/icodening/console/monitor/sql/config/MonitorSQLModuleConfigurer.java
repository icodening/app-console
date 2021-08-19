package cn.icodening.console.monitor.sql.config;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.07.29
 */
public class MonitorSQLModuleConfigurer implements ModuleRegistryConfigurer {

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsSpringServletContext = ClassUtil.exists("org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext");
        if (existsSpringServletContext) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
