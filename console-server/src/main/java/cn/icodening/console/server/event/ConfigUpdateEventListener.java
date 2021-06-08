package cn.icodening.console.server.event;

import cn.icodening.console.server.entity.ConfigurableScopeEntity;
import cn.icodening.console.server.entity.InstanceEntity;
import cn.icodening.console.server.model.PushData;
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
        final ConfigurableScopeEntity configurableScopeEntity = event.getConfigurableScopeEntity();
        final String scope = configurableScopeEntity.getScope();
        final String affectTarget = configurableScopeEntity.getAffectTarget();
        final List<InstanceEntity> instanceEntities = instanceFinderManager.find(scope, affectTarget);
        final List<String> addresses = instanceEntities
                .stream()
                .map(instanceEntity ->
                        "http://" + instanceEntity.getIp() + ":" + instanceEntity.getPort() + "/configReceiver")
                .collect(Collectors.toList());
        final PushData pushData = new PushData();
        pushData.setData(event.getSource());
        pushData.setType(configurableScopeEntity.getConfigType());
        pushData.setSendTimestamp(System.currentTimeMillis());
        notifyService.notify(pushData, addresses);
    }
}
