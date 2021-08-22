package cn.icodening.console.intercept;

import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author icodening
 * @date 2021.08.21
 */
public class InstanceMethodAroundInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceMethodAroundInterceptor.class);

    private final List<InstanceMethodInterceptor> instanceMethodInterceptors;

    public InstanceMethodAroundInterceptor(List<InstanceMethodInterceptor> instanceMethodInterceptors) {
        if (instanceMethodInterceptors == null || instanceMethodInterceptors.isEmpty()) {
            instanceMethodInterceptors = Collections.emptyList();
        }
        this.instanceMethodInterceptors = instanceMethodInterceptors;
    }

    @RuntimeType
    public Object intercept(@This Object object, @Origin Method method, @AllArguments Object[] allArguments, @Morph OverrideCallable callable) throws Exception {
        Object[] args = allArguments;
        for (InstanceMethodInterceptor instanceMethodInterceptor : instanceMethodInterceptors) {
            args = instanceMethodInterceptor.modifyArguments(allArguments);
        }
        Object ret = null;
        try {
            for (InstanceMethodInterceptor instanceMethodInterceptor : instanceMethodInterceptors) {
                instanceMethodInterceptor.beforeMethod(method, allArguments);
            }
            ret = callable.call(args);
        } catch (Throwable throwable) {
            for (int i = instanceMethodInterceptors.size() - 1; i >= 0; i--) {
                try {
                    InstanceMethodInterceptor instanceMethodInterceptor = instanceMethodInterceptors.get(i);
                    instanceMethodInterceptor.onException(throwable);
                } catch (Throwable e) {
                    throwable.addSuppressed(e);
                }
            }
            throw throwable;
        } finally {
            for (int i = instanceMethodInterceptors.size() - 1; i >= 0; i--) {
                try {
                    InstanceMethodInterceptor instanceMethodInterceptor = instanceMethodInterceptors.get(i);
                    ret = instanceMethodInterceptor.afterMethod(method, allArguments, ret);
                } catch (Throwable e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return ret;
    }
}
