package cn.icodening.console.server.aop;

import cn.icodening.console.server.common.Modifiable;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * modify advice
 * 当Modifiable类型的参数save db时，将会自动设置更新时间
 *
 * @author icodening
 * @date 2021.06.07
 */
public class ModifyEntityAdvice
        implements MethodBeforeAdvice {

    private static final String JPA_SAVE_METHOD_NAME = "save";

    private final Pointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return JPA_SAVE_METHOD_NAME.equalsIgnoreCase(method.getName());
        }
    };

    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return;
        }
        if (args[0] instanceof Modifiable) {
            ((Modifiable) args[0]).setModifyTime();
        }
    }
}
