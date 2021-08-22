package cn.icodening.console.intercept;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author icodening
 * @date 2021.08.21
 */
public interface InterceptPoint {

    ElementMatcher<MethodDescription> getMethodsMatcher();

    InstanceMethodInterceptor getMethodInterceptor();
}
