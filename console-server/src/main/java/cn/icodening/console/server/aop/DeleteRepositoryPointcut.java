package cn.icodening.console.server.aop;

import cn.icodening.console.server.repository.BaseRepository;
import cn.icodening.console.server.service.IService;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * DAO层中 {@link BaseRepository#save(Object)} delete方法的切点
 *
 * @author icodening
 * @date 2021.06.08
 */
public class DeleteRepositoryPointcut extends StaticMethodMatcherPointcut {

    private static final String JPA_SAVE_METHOD_NAME = "delete";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (!IService.class.isAssignableFrom(targetClass)) {
            return false;
        }
        return method.getName().startsWith(JPA_SAVE_METHOD_NAME);
    }
}
