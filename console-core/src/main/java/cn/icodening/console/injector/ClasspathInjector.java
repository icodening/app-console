package cn.icodening.console.injector;

/**
 * classpath注入器
 *
 * @author icodening
 * @date 2021.06.06
 */
public interface ClasspathInjector {

    /**
     * 是否应该注入当前扩展点到应用classpath下
     *
     * @return true or false, true表示注入，false表示忽略
     */
    boolean shouldInject();
}
