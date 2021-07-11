package cn.icodening.console.server.aop;

import cn.icodening.console.server.service.IService;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * Service层切点
 * {@link cn.icodening.console.server.service.IService#save(Object)}
 * {@link cn.icodening.console.server.service.IService#updateRecord(Object)} (Object)}
 * {@link cn.icodening.console.server.service.IService#addRecord(Object)} (Object)}
 *
 * @author icodening
 * @date 2021.07.11
 */
public class IServicePointcut extends StaticMethodMatcherPointcut {

    private static final String[] METHODS = new String[]{"save", "addRecord", "updateRecord"};

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (!IService.class.isAssignableFrom(targetClass)) {
            return false;
        }
        for (String methodName : METHODS) {
            if (methodName.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }
}
