package cn.icodening.console.cloud.router;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.common.entity.RouterFilterConfigEntity;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.event.ConsoleEventListener;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class RouterConfigEventListener implements ConsoleEventListener<ServerMessageReceivedEvent> {

    private static final String ROUTER_CONFIG_TYPE = RouterConfigEntity.class.getName();
    private static final String ROUTER_FILTER_CONFIG_TYPE = RouterFilterConfigEntity.class.getName();

    @Resource
    private RouterFilterConfigSource routerFilterConfigSource;

    @Resource
    private RouterConfigSource routerConfigSource;

    @Override
    public void onEvent(ServerMessageReceivedEvent event) {
        String type = event.getSource().getType();
        if (ROUTER_CONFIG_TYPE.equals(type)) {
            List<RouterConfigEntity> configs = InstanceConfigurationCache.getConfigs(RouterConfigEntity.class);
            for (RouterConfigEntity config : configs) {
                routerConfigSource.setConfig(config.getOriginService(), config);
            }
        }
        if (ROUTER_FILTER_CONFIG_TYPE.equals(type)) {
            List<RouterFilterConfigEntity> configs = InstanceConfigurationCache.getConfigs(RouterFilterConfigEntity.class);
            for (RouterFilterConfigEntity config : configs) {
                routerFilterConfigSource.setConfig(config.getServiceId(), config);
            }
        }
    }
}
