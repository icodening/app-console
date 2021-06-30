package cn.icodening.console.injector;

/**
 * classpath注入器
 *
 * @author icodening
 * @date 2021.06.06
 */
public interface ModuleRegistryConfigurer {

    /**
     * 配置模块注册器
     *
     * @param moduleRegistry 模块注册表
     */
    void configureRegistry(ModuleRegistry moduleRegistry);

}
