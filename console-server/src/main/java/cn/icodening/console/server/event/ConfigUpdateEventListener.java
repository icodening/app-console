package cn.icodening.console.server.event;

import cn.icodening.console.common.constants.URLConstants;
import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.common.model.PushData;
import cn.icodening.console.server.service.InstanceFinderManager;
import cn.icodening.console.server.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author icodening
 * @date 2021.06.08
 */
@Component
public class ConfigUpdateEventListener implements ApplicationListener<ConfigUpdateEvent> {

    @Autowired
    private InstanceFinderManager instanceFinderManager;

    @Autowired
    private NotifyService notifyService;

    @Override
    public void onApplicationEvent(ConfigUpdateEvent event) {
        final String scope = event.getScope();
        final String affectTarget = event.getAffectTarget();
        //根据该配置规则的作用范围，查询受影响的实例列表，仅通知受印象的实例列表
        //例如配置scope为 APPLICATION, affectTarget为app，那么将会通知应用名为app下的所有实例
        final List<InstanceEntity> instanceEntities = instanceFinderManager.find(scope, affectTarget);
        final List<String> addresses = instanceEntities
                .stream()
                .map(instanceEntity ->
                        "http://" + instanceEntity.getIp() + ":" + instanceEntity.getPort() + URLConstants.INSTANCE_RECEIVE_URL)
                .collect(Collectors.toList());
        final PushData<Object> pushData = new PushData<>();
        pushData.setData(event.getSource());
        pushData.setType(event.getConfigType());
        pushData.setSendTimestamp(System.currentTimeMillis());
        notifyService.notify(pushData, addresses);
    }
}
