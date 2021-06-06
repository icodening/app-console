package cn.icodening.console.injector;

import cn.icodening.console.extension.Extensible;

/**
 * classpath注入器
 *
 * @author icodening
 * @date 2021.06.06
 */
@Extensible
public interface ClasspathInjector {

    /**
     * 是否应该注入当前扩展点到应用classpath下
     *
     * @return true or false, true表示注入，false表示忽略
     */
    boolean shouldInject();
}
