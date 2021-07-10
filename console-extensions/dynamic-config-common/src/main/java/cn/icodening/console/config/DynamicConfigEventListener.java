package cn.icodening.console.config;

import cn.icodening.console.common.entity.ConfigEntity;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.ConsoleEventListener;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

/**
 * @author icodening
 * @date 2021.07.10
 */
public class DynamicConfigEventListener implements ConsoleEventListener<ServerMessageReceivedEvent> {

    private static final String RECEIVE_TYPE = ConfigEntity.class.getName();

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void onEvent(ServerMessageReceivedEvent event) {
        ServerMessage source = event.getSource();
        if (RECEIVE_TYPE.equalsIgnoreCase(source.getType())) {
            List<ConfigEntity> configs = InstanceConfigurationCache.getConfigs(ConfigEntity.class);
            Properties properties = new Properties();
            for (ConfigEntity config : configs) {
                String content = config.getContent();
                try {
                    properties.load(new StringReader(content));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            applicationContext.publishEvent(new ConfigChangeEvent(properties));
        }
    }
}
