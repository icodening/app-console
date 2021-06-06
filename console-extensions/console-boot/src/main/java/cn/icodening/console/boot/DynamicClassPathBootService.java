package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.extension.ExtensionLoader;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class DynamicClassPathBootService extends BaseBootService {

    @Override
    public void start() throws AppConsoleException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(DynamicClassPathBootService.class.getName() + ": dynamic class path");
        if (contextClassLoader instanceof URLClassLoader) {
            List<ClassPathConfigurer> classPathConfigurers = ExtensionLoader.getExtensionLoader(ClassPathConfigurer.class).getAllExtension();
            ClassPathRegistry classPathRegistry = new ClassPathRegistry();
            for (ClassPathConfigurer classPathConfigurer : classPathConfigurers) {
                classPathConfigurer.configurerClassPath(classPathRegistry);
            }
            try {
                Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                List<String> allURL = classPathRegistry.getAllURL();
                for (String u : allURL) {
                    System.out.println(u);
                    URL url = new URL(u);
                    addURL.invoke(contextClassLoader, url);
                }
                if (allURL.isEmpty()) {
                    System.out.println(DynamicClassPathBootService.class.getName() + ": dynamic class path url is empty");
                }
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
    }
}
