package cn.icodening.console.server.aop;

import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import cn.icodening.console.server.event.ConfigUpdateEvent;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ConfigurableScopeEntityAdvice
        implements AfterReturningAdvice {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor pushExecutor;

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return;
        }
        if (args[0] instanceof ConfigurableScopeEntity) {
            //获取作用域
            final String scope = ((ConfigurableScopeEntity) args[0]).getScope();
            //获取影响目标
            final String affectTarget = ((ConfigurableScopeEntity) args[0]).getAffectTarget();
            if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
                List<ConfigurableScopeEntity> entities = new ArrayList<>(1);
                entities.add((ConfigurableScopeEntity) args[0]);
                CompletableFuture.runAsync(() ->
                        applicationContext.publishEvent(new ConfigUpdateEvent(((ConfigurableScopeEntity) args[0]).configType(), scope, affectTarget, entities)), pushExecutor);
            }
        }
    }
}
