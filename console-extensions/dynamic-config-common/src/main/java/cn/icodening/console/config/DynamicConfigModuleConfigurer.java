package cn.icodening.console.config;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.07.10
 */
public class DynamicConfigModuleConfigurer implements ModuleRegistryConfigurer {

    private static final String SPRING_CLOUD_CONFIG_CLASS_NAME = "org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration";

    private static final String SPRING_BOOT_CONFIG_CLASS_NAME = "org.springframework.boot.SpringApplication";

    private static final String SPRING_SERVLET_CONTEXT = "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsCloudConfig = ClassUtil.exists(SPRING_CLOUD_CONFIG_CLASS_NAME);
        boolean existsSpringBoot = ClassUtil.exists(SPRING_BOOT_CONFIG_CLASS_NAME);
        boolean existsSpringServletContext = ClassUtil.exists(SPRING_SERVLET_CONTEXT);
        if (!existsSpringBoot || !existsSpringServletContext) {
            return;
        }
        moduleRegistry.registerCurrentModule();
        if (!existsCloudConfig) {
            //注入springboot适配模块
            moduleRegistry.registerWithModuleName("dynamic-config-springboot-adapter");
        } else {
            //TODO 注入spring cloud适配模块
        }
    }

}
