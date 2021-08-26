package cn.icodening.console.boot.spring;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.intercept.PrepareClassLoadInterceptor;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerAdapter;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.logger.jdk.JDKLoggerAdapter;
import cn.icodening.console.logger.log4j2.Log4j2LoggerAdapter;
import cn.icodening.console.logger.logback.LogbackLoggerAdapter;
import cn.icodening.console.util.AgentStartHelper;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import cn.icodening.console.util.ReflectUtil;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

/**
 * @author icodening
 * @date 2021.08.26
 */
public class PrepareLoadSpringApplicationInterceptor implements PrepareClassLoadInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareLoadSpringApplicationInterceptor.class);

    private static final String CLASSNAME = "org.springframework.boot.SpringApplication";

    private static final String[] REQUIRED_MODULES = {"console-boot"};

    private static final String[] LOGGER_CLASSES = {"cn.icodening.console.logger.logback.LogbackLoggerAdapter"};

    @Override
    public String type() {
        return CLASSNAME;
    }

    @Override
    public void prepare(ClassLoader classLoader) {
        List<Class<? extends LoggerAdapter>> candidates = Arrays.asList(
                LogbackLoggerAdapter.class, Log4j2LoggerAdapter.class, JDKLoggerAdapter.class
        );
        for (Class<? extends LoggerAdapter> clazz : candidates) {
            try {
                LoggerFactory.setLoggerAdapter(clazz.newInstance());
                break;
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }
        }
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
//        LoggerFactory.refreshLoggerAdapter();
    }

    private static void registerRequiredModule(ModuleRegistry moduleRegistry) {
        for (String requiredModule : REQUIRED_MODULES) {
            moduleRegistry.registerWithModuleName(requiredModule);
        }
    }

}
