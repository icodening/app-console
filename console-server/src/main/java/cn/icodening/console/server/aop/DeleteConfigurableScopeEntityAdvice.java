package cn.icodening.console.server.aop;

import cn.icodening.console.common.constants.ServerMessageAction;
import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import cn.icodening.console.server.event.ConfigUpdateEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class DeleteConfigurableScopeEntityAdvice
        implements MethodInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor pushExecutor;

    private final ThreadLocal<List<ConfigurableScopeEntity>> configListLocal = ThreadLocal.withInitial(() -> new ArrayList<>());

    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return;
        }
        if (args[0] instanceof Number
                && target instanceof JpaRepository) {
            Optional byId = ((JpaRepository) target).findById(args[0]);
            byId.ifPresent(entity -> {
                if (entity instanceof ConfigurableScopeEntity) {
                    String scope = ((ConfigurableScopeEntity) entity).getScope();
                    String affectTarget = ((ConfigurableScopeEntity) entity).getAffectTarget();
                    if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
                        List<ConfigurableScopeEntity> entities = new ArrayList<>(1);
                        entities.add((ConfigurableScopeEntity) entity);
                        configListLocal.set(entities);
                    }
                }
            });
        }
    }

    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return;
        }
        List<ConfigurableScopeEntity> configurableScopeEntities = configListLocal.get();
        if (configurableScopeEntities == null || configurableScopeEntities.isEmpty()) {
            return;
        }
        ConfigurableScopeEntity configurableScopeEntity = configurableScopeEntities.get(0);
        //获取作用域
        final String scope = configurableScopeEntity.getScope();
        //获取影响目标
        final String affectTarget = configurableScopeEntity.getAffectTarget();
        if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
            CompletableFuture.runAsync(() ->
                    applicationContext.publishEvent(new ConfigUpdateEvent(ServerMessageAction.DELETE,
                            configurableScopeEntity.configType(), scope, affectTarget, configurableScopeEntities)), pushExecutor);
        }
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object ret;
        try {
            this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
            ret = mi.proceed();
            this.afterReturning(ret, mi.getMethod(), mi.getArguments(), mi.getThis());
        } finally {
            configListLocal.remove();
        }
        return ret;
    }
}
