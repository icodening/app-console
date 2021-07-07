package cn.icodening.console.server.aop;

import cn.icodening.console.common.constants.ServerMessageAction;
import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import cn.icodening.console.server.event.ConfigUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.07.08
 */
public class SaveConfigurableScopeEntityAdvice extends AbstractConfigurableScopeEntityAdvice {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor pushExecutor;

    @Override
    protected List<ConfigurableScopeEntity> afterReturning(Object returnValue, Method method, Object[] args, Object target, List<ConfigurableScopeEntity> before) throws Throwable {
        if (args.length != 1) {
            return before;
        }
        if (args[0] instanceof ConfigurableScopeEntity) {
            //获取作用域
            final String scope = ((ConfigurableScopeEntity) args[0]).getScope();
            //获取影响目标
            final String affectTarget = ((ConfigurableScopeEntity) args[0]).getAffectTarget();
            if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
                before.add((ConfigurableScopeEntity) args[0]);
            }
        }
        //TODO 批量新增的情况
        return before;
    }

    @Override
    protected void processScopeTarget(String configType, String scope, String affectTarget, List<ConfigurableScopeEntity> source) {
        CompletableFuture.runAsync(() ->
                applicationContext.publishEvent(new ConfigUpdateEvent(ServerMessageAction.UPDATE, configType, scope, affectTarget, source)), pushExecutor);
    }
}
