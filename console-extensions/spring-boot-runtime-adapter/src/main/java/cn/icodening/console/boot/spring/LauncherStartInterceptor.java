package cn.icodening.console.boot.spring;

import cn.icodening.console.intercept.InstanceMethodInterceptor;
import cn.icodening.console.util.AgentStartHelper;

import java.lang.reflect.Method;

/**
 * @author icodening
 * @date 2021.08.22
 */
public class LauncherStartInterceptor implements InstanceMethodInterceptor {

    @Override
    public void beforeMethod(Method method, Object[] allArguments) {
        AgentStartHelper.start();
    }
}
