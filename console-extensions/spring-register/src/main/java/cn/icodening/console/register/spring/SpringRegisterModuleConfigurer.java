package cn.icodening.console.register.spring;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import org.springframework.boot.SpringApplication;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class SpringRegisterModuleConfigurer implements ModuleRegistryConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringRegisterModuleConfigurer.class);

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        try {
            Class.forName(SpringApplication.DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
            moduleRegistry.registerCurrentModule();
        } catch (ClassNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
