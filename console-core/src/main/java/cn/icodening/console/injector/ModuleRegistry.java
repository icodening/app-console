package cn.icodening.console.injector;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author icodening
 * @date 2021.06.29
 */
public class ModuleRegistry {

    /**
     * 即将被注册的扩展包
     * module -> jar
     */
    private final Map<String, JarFile> jarsRegistry = new HashMap<>();

    /**
     * 所有加载到的扩展包
     * module -> jar
     */
    private final Map<String, JarFile> allLoadedJars;

    private final List<JarFile> loadedJars = new LinkedList<>();

    public ModuleRegistry(Map<String, JarFile> allLoadedJars) {
        this.allLoadedJars = allLoadedJars;
        this.loadedJars.addAll(allLoadedJars.values());
    }

    public Map<String, JarFile> getRegisteredModule() {
        return new HashMap<>(jarsRegistry);
    }

    /**
     * 根据类名来注册对应的扩展包
     *
     * @param classname 全类名
     */
    public void registerWithClassName(String classname) {
        if (classname == null) {
            return;
        }
        for (JarFile loadedJar : loadedJars) {
            JarEntry jarEntry = loadedJar.getJarEntry(classname.replace('.', '/').concat(".class"));
            if (jarEntry != null) {
                try {
                    String value = loadedJar.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
                    jarsRegistry.put(value, loadedJar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据模块名注册对应模块
     *
     * @param moduleName 模块名
     */
    public void registerWithModuleName(String moduleName) {
        if (moduleName == null) {
            return;
        }
        JarFile jarFile = allLoadedJars.get(moduleName);
        try {
            String module = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            if (module != null) {
                jarsRegistry.put(module, jarFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册当前调用该方法的类所在的模块
     */
    public void registerCurrentModule() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String className = stackTrace[1].getClassName();
        registerWithClassName(className);
    }

    /**
     * 从已注册列表中移除某一模块
     *
     * @param moduleName 模块名
     */
    public void remove(String moduleName) {
        jarsRegistry.remove(moduleName);
    }
}
