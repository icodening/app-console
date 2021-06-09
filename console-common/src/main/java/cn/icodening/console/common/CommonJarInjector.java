package cn.icodening.console.common;

import cn.icodening.console.injector.ClasspathInjector;

/**
 * @author icodening
 * @date 2021.06.10
 */
public class CommonJarInjector implements ClasspathInjector {
    @Override
    public boolean shouldInject() {
        return true;
    }
}
