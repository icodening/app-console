package cn.icodening.console.server.aop;

import cn.icodening.console.common.Modifiable;
import org.springframework.aop.MethodBeforeAdvice;

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

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return;
        }
        //FIXME save list
        if (args[0] instanceof Modifiable) {
            ((Modifiable) args[0]).setModifyTime();
        }
    }
}
