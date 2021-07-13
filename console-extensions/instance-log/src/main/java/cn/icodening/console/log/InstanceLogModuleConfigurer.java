package cn.icodening.console.log;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class InstanceLogModuleConfigurer implements ModuleRegistryConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLogModuleConfigurer.class);

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            //如取不到websocket容器则忽略
            if (webSocketContainer == null) {
                LOGGER.warn("no websocket dependency, can't use log viewer online !");
                return;
            }
            moduleRegistry.registerCurrentModule();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
