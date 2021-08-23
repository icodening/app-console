package cn.icodening.console.agent;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.boot.BootServiceManager;
import cn.icodening.console.extension.ExtensionClassLoader;
import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.AgentStartHelper;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import cn.icodening.console.util.ReflectUtil;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.jar.JarFile;

/**
 * @author icodening
 * @date 2021.05.20
 */
public class AppConsoleAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConsoleAgent.class);

    /**
     * 必备模块名
     */
    private static final String[] REQUIRED_MODULES = {"console-boot"};

    private static final String DELAY_START_KEY = "delay.start.agent";

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        agentInitializer(agentArgs, instrumentation);
        boolean delay = Boolean.parseBoolean(System.getProperty(DELAY_START_KEY, "false"));
        if (!delay) {
            addRequiredDependency();
            startAgent(agentArgs, instrumentation);
        } else {
            AgentStartHelper.setStart(() -> startAgent(agentArgs, instrumentation));
        }
    }

    private static void agentInitializer(String agentArgs, Instrumentation instrumentation) {
        try {
            ServiceLoader<AgentInitializer> load = ServiceLoader.load(AgentInitializer.class, ExtensionClassLoaderHolder.get());
            for (AgentInitializer agentInitializer : load) {
                agentInitializer.initialize(agentArgs, instrumentation);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private static void startAgent(String agentArgs, Instrumentation instrumentation) {
        LOGGER.info("app console agent start, version is v" + AppConsoleAgent.class.getPackage().getImplementationVersion());
        // 启动所有服务扩展点
        try {
            BootServiceManager.startBootServices();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }

        // TODO Extension

        // 安全销毁所有服务扩展点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                BootServiceManager.destroyBootServices();
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }));
    }

    /**
     * 添加必须依赖
     */
    private static void addRequiredDependency() {
        ExtensionClassLoader classLoader = ExtensionClassLoaderHolder.get();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ServiceLoader<ModuleRegistryConfigurer> load = ServiceLoader.load(ModuleRegistryConfigurer.class, classLoader);
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
    }

    /**
     * 注册必备模块
     */
    private static void registerRequiredModule(ModuleRegistry moduleRegistry) {
        for (String requiredModule : REQUIRED_MODULES) {
            moduleRegistry.registerWithModuleName(requiredModule);
        }
    }
}
