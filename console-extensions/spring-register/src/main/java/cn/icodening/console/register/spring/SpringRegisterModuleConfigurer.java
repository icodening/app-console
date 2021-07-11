package cn.icodening.console.register.spring;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class SpringRegisterModuleConfigurer implements ModuleRegistryConfigurer {

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsSpringServletContext = ClassUtil.exists("org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext");
        if (existsSpringServletContext) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
