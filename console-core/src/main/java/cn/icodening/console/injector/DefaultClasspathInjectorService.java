package cn.icodening.console.injector;

import cn.icodening.console.AgentPath;
import cn.icodening.console.AppConsoleException;
import cn.icodening.console.boot.BaseBootService;
import cn.icodening.console.extension.ExtensionClassLoader;
import cn.icodening.console.extension.ExtensionLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class DefaultClasspathInjectorService extends BaseBootService {

    @Override
    public void start() throws AppConsoleException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader instanceof URLClassLoader) {
            List<ClasspathInjector> classPathConfigurers = ExtensionLoader.getExtensionLoader(ClasspathInjector.class).getAllExtension();
            ClassLoader classpathInjectorClassLoader = ClasspathInjector.class.getClassLoader();
            ExtensionClassLoader classLoader;
            if (!(classpathInjectorClassLoader instanceof ExtensionClassLoader)) {
                classLoader = new ExtensionClassLoader(contextClassLoader);
                classLoader.addPath(new File(AgentPath.INSTANCE.getPath() + "/extensions"));
            } else {
                classLoader = ((ExtensionClassLoader) classpathInjectorClassLoader);
            }

            ClasspathRegistry classPathRegistry = new ClasspathRegistry();
            for (ClasspathInjector classPathConfigurer : classPathConfigurers) {
                String jarPathByClass = classLoader.getJarPathByClass(classPathConfigurer.getClass().getName().replace('.', '/').concat(".class"));
                if (jarPathByClass != null
                        && classPathConfigurer.shouldInject()) {
                    classPathRegistry.addUrl("file:" + jarPathByClass);
                }
            }
            try {
                Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
                List<String> allUrls = classPathRegistry.getAllUrl();
                for (String u : allUrls) {
                    System.out.println(u);
                    URL url = new URL(u);
                    addUrlMethod.invoke(contextClassLoader, url);
                }
                if (allUrls.isEmpty()) {
                    System.out.println(DefaultClasspathInjectorService.class.getName() + ": dynamic class path url is empty");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}