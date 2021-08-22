package cn.icodening.console.intercept;

import java.lang.reflect.Method;

/**
 * @author icodening
 * @date 2021.08.21
 */
public interface InstanceMethodInterceptor {

    /**
     * 修改方法入参
     *
     * @param allArguments 调用被拦截方法的原参数
     * @return 修改后的参数列表
     */
    default Object[] modifyArguments(Object[] allArguments) {
        return allArguments;
    }

    /**
     * 方法调用前拦截
     *
     * @param method       当前调用的方法
     * @param allArguments 参数列表
     */
    default void beforeMethod(Method method, Object[] allArguments) {

    }

    /**
     * 方法执行异常时处理
     *
     * @param throwable 异常
     */
    default void onException(Throwable throwable) {
    }

    /**
     * method after return advice
     *
     * @param method       当前调用的方法
     * @param allArguments 参数列表
     * @param result       原方法返回值
     * @return 返回值
     */
    default Object afterMethod(Method method, Object[] allArguments, Object result) {
        return result;
    }


}
