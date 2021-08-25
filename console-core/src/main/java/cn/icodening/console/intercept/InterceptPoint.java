package cn.icodening.console.intercept;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * 拦截点
 *
 * @author icodening
 * @date 2021.08.21
 */
public interface InterceptPoint {

    /**
     * 获取方法匹配器
     *
     * @return 方法匹配器
     */
    default ElementMatcher<MethodDescription> getMethodsMatcher() {
        return ElementMatchers.none();
    }

    /**
     * 获取方法拦截器
     *
     * @return 方法拦截器
     */
    default InstanceMethodInterceptor getMethodInterceptor() {
        return null;
    }

    /**
     * 获取构造器匹配器
     *
     * @return 构造器匹配器
     */
    default ElementMatcher<MethodDescription> getConstructorMatcher() {
        ElementMatchers.isConstructor().matches(MethodDescription.UNDEFINED);
        return ElementMatchers.none();
    }

    /**
     * 获取构造器拦截器
     *
     * @return 构造器拦截器
     */
    default ConstructorInterceptor getConstructorInterceptor() {
        return null;
    }

}
