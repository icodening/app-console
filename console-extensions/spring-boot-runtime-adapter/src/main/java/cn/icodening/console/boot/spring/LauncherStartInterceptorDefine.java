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
public class LauncherStartInterceptorDefine implements InterceptorDefine {

    private static final String MAIN_RUNNER_CLASS = "org.springframework.boot.loader.MainMethodRunner";

    @Override
    public String type() {
        return MAIN_RUNNER_CLASS;
    }

    @Override
    public InterceptPoint[] getInterceptPoints() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named("run");
                    }

                    @Override
                    public InstanceMethodInterceptor getMethodInterceptor() {
                        return new LauncherStartInterceptor();
                    }
                }
        };
    }
}
