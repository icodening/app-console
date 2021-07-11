package cn.icodening.console.agent;

import cn.icodening.console.AgentStartEventProvider;
import cn.icodening.console.boot.BootServiceManager;
import cn.icodening.console.event.AgentStartEvent;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.extension.ExtensionClassLoader;
import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import cn.icodening.console.util.ReflectUtil;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
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
    private static final String[] REQUIRED_MODULES = {"console-common", "console-boot"};

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        addRequiredDependency();
        agentInitializer(agentArgs);
        List<AgentStartEvent> agentStartEvents = getAgentStartEventList();
        if (agentStartEvents.isEmpty()) {
            //直接启动
            startAgent(agentArgs, instrumentation);
            return;
        }
        //回调启动
        EventDispatcher.registerOnceEvent(agentStartEvents.get(0).getClass(), event -> {
            ((AgentStartEvent) event).invoke(agentArgs, instrumentation);
            startAgent(agentArgs, instrumentation);
        });
    }

    private static List<AgentStartEvent> getAgentStartEventList() {
        ExtensionClassLoader classLoader = ExtensionClassLoaderHolder.get();
        ServiceLoader<AgentStartEventProvider> load = ServiceLoader.load(AgentStartEventProvider.class, classLoader);
        Iterator<AgentStartEventProvider> iterator = load.iterator();
        List<AgentStartEvent> agentStartEvents = new ArrayList<>(4);
        iterator.forEachRemaining(event -> {
            if (event.get() != null) {
                agentStartEvents.add(event.get());
            }
        });
        return agentStartEvents;
    }

    private static void agentInitializer(String agentArgs) {
        try {
            BootServiceManager.initBootServices(agentArgs);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private static void startAgent(String agentArgs, Instrumentation instrumentation) {
        LOGGER.info("app console agent start, version is v" + AppConsoleAgent.class.getPackage().getImplementationVersion());
        // 启动所有服务扩展点
        try {
            BootServiceManager.initBootServices(agentArgs);
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
        ModuleRegistry moduleRegistry = buildLoadedModuleRegistry();
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

    private static ModuleRegistry buildLoadedModuleRegistry() {
        List<JarFile> loadedJars = ExtensionClassLoaderHolder.get().getLoadedJars();
        Map<String, JarFile> loaded = new HashMap<>();
        for (JarFile loadedJar : loadedJars) {
            try {
                String moduleName = loadedJar.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
                loaded.putIfAbsent(moduleName, loadedJar);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        return new ModuleRegistry(loaded);
    }
}
