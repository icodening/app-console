package cn.icodening.console.server.aop;

import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 抽象更新数据通知点
 *
 * @author icodening
 * @date 2021.07.08
 */
public abstract class AbstractConfigurableScopeEntityAdvice
        implements MethodInterceptor {

    /**
     * 方法执行前，收集更新列表
     *
     * @param method 方法
     * @param args   方法参数
     * @param target this
     * @return
     * @throws Throwable 异常
     */
    protected List<ConfigurableScopeEntity> beforeMethod(Method method, Object[] args, Object target) throws Throwable {
        return new ArrayList<>(1);
    }

    /**
     * 方法执行后，收集更新列表
     *
     * @param returnValue  方法返回值
     * @param method       方法
     * @param args         方法参数
     * @param target       调用method的对象
     * @param beforeSource 方法调用前收集到的更新列表
     * @return
     * @throws Throwable
     */
    protected List<ConfigurableScopeEntity> afterReturning(Object returnValue, Method method, Object[] args, Object target, List<ConfigurableScopeEntity> beforeSource) throws Throwable {
        return beforeSource;
    }

    @Override
    public final Object invoke(MethodInvocation mi) throws Throwable {
        Object ret;
        List<ConfigurableScopeEntity> before = this.beforeMethod(mi.getMethod(), mi.getArguments(), mi.getThis());
        ret = mi.proceed();
        List<ConfigurableScopeEntity> after = this.afterReturning(ret, mi.getMethod(), mi.getArguments(), mi.getThis(), before);
        Set<ScopeTarget> scopeTargets = new HashSet<>();
        for (ConfigurableScopeEntity configurableScopeEntity : after) {
            //获取作用域
            String scope = configurableScopeEntity.getScope();
            //获取影响目标
            String affectTarget = configurableScopeEntity.getAffectTarget();
            //获取配置类型
            String configType = configurableScopeEntity.configType();
            ScopeTarget scopeTarget = new ScopeTarget(scope, affectTarget, configType);
            if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
                scopeTargets.add(scopeTarget);
            }
        }
        for (ScopeTarget scopeTarget : scopeTargets) {
            processScopeTarget(scopeTarget.configType, scopeTarget.scope, scopeTarget.affectTarget, after);
        }
        return ret;
    }

    /**
     * 处理影响域目标
     *
     * @param configType   被改变的配置类型
     * @param scope        影响域，APPLICATION、INSTANCE
     * @param affectTarget 影响目标值
     * @param source       更新源
     */
    protected abstract void processScopeTarget(String configType, String scope, String affectTarget, List<ConfigurableScopeEntity> source);

    private static class ScopeTarget {
        private final String scope;

        private final String affectTarget;

        private final String configType;

        public ScopeTarget(String scope, String affectTarget, String configType) {
            this.scope = scope;
            this.affectTarget = affectTarget;
            this.configType = configType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScopeTarget that = (ScopeTarget) o;
            return scope.equals(that.scope) &&
                    affectTarget.equals(that.affectTarget) &&
                    configType.equals(that.configType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scope, affectTarget, configType);
        }
    }
}
