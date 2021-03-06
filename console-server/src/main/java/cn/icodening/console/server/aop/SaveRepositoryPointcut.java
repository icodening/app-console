package cn.icodening.console.server.aop;

import cn.icodening.console.server.repository.BaseRepository;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * DAO层中 {@link BaseRepository#save(Object)} save方法的切点
 *
 * @author icodening
 * @date 2021.06.08
 */
public class SaveRepositoryPointcut extends StaticMethodMatcherPointcut {

    private static final String JPA_SAVE_METHOD_NAME = "save";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (!BaseRepository.class.isAssignableFrom(targetClass)) {
            return false;
        }
        return method.getName().startsWith(JPA_SAVE_METHOD_NAME);
    }
}
