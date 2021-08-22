package cn.icodening.console.boot.spring;

import cn.icodening.console.intercept.InstanceMethodInterceptor;
import cn.icodening.console.intercept.InterceptPoint;
import cn.icodening.console.intercept.InterceptorDefine;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author icodening
 * @date 2021.08.22
 */
public class LauncherCreateClassLoaderInterceptorDefine implements InterceptorDefine {

    private static final String LAUNCHER_CLASS_NAME = "org.springframework.boot.loader.Launcher";

    private static final String INTERCEPTOR_METHOD_NAME = "createClassLoader";

    @Override
    public String type() {
        return LAUNCHER_CLASS_NAME;
    }

    @Override
    public InterceptPoint[] getInterceptPoints() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(INTERCEPTOR_METHOD_NAME);
                    }

                    @Override
                    public InstanceMethodInterceptor getMethodInterceptor() {
                        return new LauncherCreateClassLoaderInterceptor();
                    }
                }
        };
    }
}
