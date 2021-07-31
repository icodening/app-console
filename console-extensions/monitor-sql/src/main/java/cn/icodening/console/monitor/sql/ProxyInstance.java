package cn.icodening.console.monitor.sql;

/**
 * 用来标识代理对象，可用来缓存一些数据，尽可能避免使用ThreadLocal传递
 *
 * @author icodening
 * @date 2021.07.30
 */
public interface ProxyInstance<T> {

    /**
     * 赋值动态字段
     *
     * @param field 缓存字段
     */
    void setDynamicField(T field);

    /**
     * 获取动态字段
     *
     * @return 缓存字段
     */
    T getDynamicField();
}
