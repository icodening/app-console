package cn.icodening.console.intercept;

import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 对象实例化后回调拦截器
 *
 * @author icodening
 * @date 2021.08.25
 */
public class AfterConstructorInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfterConstructorInterceptor.class);

    private final List<ConstructorInterceptor> constructorInterceptors;

    public AfterConstructorInterceptor(List<ConstructorInterceptor> constructorInterceptors) {
        if (constructorInterceptors == null || constructorInterceptors.isEmpty()) {
            constructorInterceptors = Collections.emptyList();
        }
        this.constructorInterceptors = constructorInterceptors;
    }

    @RuntimeType
    public void onConstructor(@This Object obj, @AllArguments Object[] allArguments) {
        try {
            for (ConstructorInterceptor constructorInterceptor : constructorInterceptors) {
                constructorInterceptor.afterConstructor(obj, allArguments);
            }
        } catch (Throwable t) {
            String msg = String.format("new instance success, but [%s] on constructor fail, constructor arguments is " + Arrays.toString(allArguments), obj);
            LOGGER.warn(msg, t);
        }

    }
}
