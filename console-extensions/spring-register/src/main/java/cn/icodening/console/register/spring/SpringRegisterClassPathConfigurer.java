package cn.icodening.console.register.spring;

import cn.icodening.console.boot.ClassPathConfigurer;
import cn.icodening.console.boot.ClassPathRegistry;
import cn.icodening.console.extension.ExtensionClassLoader;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class SpringRegisterClassPathConfigurer implements ClassPathConfigurer {

    @Override
    public void configurerClassPath(ClassPathRegistry classPathRegistry) {
        try {
            System.out.println(SpringRegisterClassPathConfigurer.class.getName() + ": prepare");
            ClassLoader classLoader = ClassPathConfigurer.class.getClassLoader();
            if (classLoader instanceof ExtensionClassLoader) {
                ExtensionClassLoader extensionClassLoader = ((ExtensionClassLoader) classLoader);
                String jarPathByClass = extensionClassLoader.getJarPathByClass(SpringRegisterClassPathConfigurer.class.getName().replace('.', '/').concat(".class"));
                if (jarPathByClass != null) {
                    classPathRegistry.addURL("file:" + jarPathByClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
