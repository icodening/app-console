package cn.icodening.console.intercept;

/**
 * 类加载之前的拦截器
 *
 * @author icodening
 * @date 2021.08.26
 */
public interface PrepareClassLoadInterceptor extends InterceptTypes {

    /**
     * 准备类加载
     *
     * @param classLoader 类加载器
     */
    void prepare(ClassLoader classLoader);
}
