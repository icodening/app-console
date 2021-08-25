package cn.icodening.console.intercept;

/**
 * @author icodening
 * @date 2021.08.25
 */
public interface ConstructorInterceptor {

    /**
     * 实例化对象完成后执行
     *
     * @param target  实例化完成的对象
     * @param allArgs 当前构造方法的参数列表
     */
    void afterConstructor(Object target, Object[] allArgs);
}
