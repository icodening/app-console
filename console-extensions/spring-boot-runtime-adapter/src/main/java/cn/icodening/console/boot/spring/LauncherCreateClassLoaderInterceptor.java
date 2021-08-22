package cn.icodening.console.boot.spring;

import cn.icodening.console.AgentPath;
import cn.icodening.console.extension.ExtensionClassLoader;
import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.intercept.InstanceMethodInterceptor;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

/**
 * @author icodening
 * @date 2021.08.21
 */
public class LauncherCreateClassLoaderInterceptor implements InstanceMethodInterceptor {

    private static final String[] REQUIRED_MODULES = {"console-boot"};

    @Override
    public Object[] modifyArguments(Object[] allArguments) {
        if (allArguments == null
                || allArguments.length != 1
                || !URL[].class.isAssignableFrom(allArguments[0].getClass())) {
            return allArguments;
        }

        URL[] us = (URL[]) allArguments[0];
        List<URL> urls = new ArrayList<>(Arrays.asList(us));
        ClassLoader originLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader classLoader = new URLClassLoader(us, originLoader);
        ExtensionClassLoader urlClassLoader = new ExtensionClassLoader(classLoader);

        urlClassLoader.addPath(new File(AgentPath.INSTANCE.getPath() + "/extensions"));
        ServiceLoader<ModuleRegistryConfigurer> load = ServiceLoader.load(ModuleRegistryConfigurer.class, urlClassLoader);

        Iterator<ModuleRegistryConfigurer> iterator = load.iterator();
        ModuleRegistry moduleRegistry = ModuleRegistry.buildLoadedModuleRegistry();
        registerRequiredModule(moduleRegistry);

        Thread.currentThread().setContextClassLoader(urlClassLoader);
        try {
            while (iterator.hasNext()) {
                ModuleRegistryConfigurer registryConfigurer = iterator.next();
                registryConfigurer.configureRegistry(moduleRegistry);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(originLoader);
        }


        Map<String, JarFile> registeredModule = moduleRegistry.getRegisteredModule();
        StringBuilder enableModules = new StringBuilder();
        registeredModule.forEach((module, jar) -> {
            try {
                enableModules.append("[").append(module).append("] ");
                URL url = new URL("file:" + jar.getName());
                urls.add(url);
            } catch (Exception ignore) {
            }
        });

        Object[] newArgs = new Object[]{urls.toArray(new URL[0])};
        System.out.println("enable modules: " + enableModules);
        return newArgs;
    }

    private static void registerRequiredModule(ModuleRegistry moduleRegistry) {
        for (String requiredModule : REQUIRED_MODULES) {
            moduleRegistry.registerWithModuleName(requiredModule);
        }
    }
}
