package cn.icodening.console.server.aop;

import cn.icodening.console.common.constants.ServerMessageAction;
import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import cn.icodening.console.server.event.ConfigUpdateEvent;
import cn.icodening.console.server.service.IService;
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
 * @date 2021.07.08
 */
public class DeleteConfigurableScopeEntityAdvice extends AbstractConfigurableScopeEntityAdvice {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor pushExecutor;

    /**
     * delete方法调用前获取即将被删除的记录信息，获取其影响范围、影响实例，用以预备通知受影响的实例
     *
     * @param method 方法
     * @param args   方法参数
     * @param target this
     * @return
     * @throws Throwable
     */
    @Override
    public List<ConfigurableScopeEntity> beforeMethod(Method method, Object[] args, Object target) throws Throwable {
        if (args.length != 1) {
            return null;
        }
        List<ConfigurableScopeEntity> entities = new ArrayList<>(1);
        if (args[0] instanceof Number
                && target instanceof IService) {
            Object entity = ((IService) target).findById((Long) args[0]);
            if (entity != null && entity instanceof ConfigurableScopeEntity) {
                String scope = ((ConfigurableScopeEntity) entity).getScope();
                String affectTarget = ((ConfigurableScopeEntity) entity).getAffectTarget();
                if (StringUtils.hasText(scope) && StringUtils.hasText(affectTarget)) {
                    entities.add((ConfigurableScopeEntity) entity);
                }
            }
        }
        //TODO 批量删除的情况
        return entities;
    }

    @Override
    protected void processScopeTarget(String configType, String scope, String affectTarget, List<ConfigurableScopeEntity> source) {
        CompletableFuture.runAsync(() ->
                applicationContext.publishEvent(new ConfigUpdateEvent(ServerMessageAction.DELETE,
                        configType, scope, affectTarget, source)), pushExecutor);
    }
}
