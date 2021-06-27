package cn.icodening.console.boot;

import cn.icodening.console.injector.ClasspathInjector;

/**
 * @author icodening
 * @date 2021.06.27
 */
public class BootModuleInjector implements ClasspathInjector {

    @Override
    public boolean shouldInject() {
        return true;
    }
}
