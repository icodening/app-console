package cn.icodening.console.boot.spring;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.intercept.PrepareClassLoadInterceptor;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.AgentStartHelper;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import cn.icodening.console.util.ReflectUtil;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.jar.JarFile;

/**
 * 加载 {@link org.springframework.boot.SpringApplication} 类的拦截器，主要用于加载需要的模块并启动agent
 *
 * @author icodening
 * @date 2021.08.26
 */
public class PrepareLoadSpringApplicationInterceptor implements PrepareClassLoadInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareLoadSpringApplicationInterceptor.class);

    private static final String CLASSNAME = "org.springframework.boot.SpringApplication";

    private static final String[] REQUIRED_MODULES = {"console-boot"};

    @Override
    public String type() {
        return CLASSNAME;
    }

    @Override
    public void prepare(ClassLoader classLoader) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ServiceLoader<ModuleRegistryConfigurer> load = ServiceLoader.load(ModuleRegistryConfigurer.class, ExtensionClassLoaderHolder.get());
        Iterator<ModuleRegistryConfigurer> iterator = load.iterator();
        Method addUrlMethod = ReflectUtil.findMethod(URLClassLoader.class, "addURL", URL.class);
        addUrlMethod.setAccessible(true);
        ModuleRegistry moduleRegistry = ModuleRegistry.buildLoadedModuleRegistry();
        registerRequiredModule(moduleRegistry);
        while (iterator.hasNext()) {
            ModuleRegistryConfigurer registryConfigurer = iterator.next();
            registryConfigurer.configureRegistry(moduleRegistry);
        }
        Map<String, JarFile> registeredModule = moduleRegistry.getRegisteredModule();
        StringBuilder enableModules = new StringBuilder();
        registeredModule.forEach((module, jar) -> {
            try {
                enableModules.append("[").append(module).append("] ");
                URL url = new URL("file:" + jar.getName());
                addUrlMethod.invoke(contextClassLoader, url);
            } catch (Exception e) {
                LOGGER.warn("install module error [" + module + "]");
            }
        });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("enable modules: " + enableModules.toString());
        }
        AgentStartHelper.start();
    }

    private static void registerRequiredModule(ModuleRegistry moduleRegistry) {
        for (String requiredModule : REQUIRED_MODULES) {
            moduleRegistry.registerWithModuleName(requiredModule);
        }
    }

}
