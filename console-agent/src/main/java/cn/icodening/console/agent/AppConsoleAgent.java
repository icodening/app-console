package cn.icodening.console.agent;

import cn.icodening.console.boot.BootServiceManager;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.05.20
 */
public class AppConsoleAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConsoleAgent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        LOGGER.info("app console agent start");
        // 启动所有服务扩展点
        try {
            BootServiceManager.initBootServices(agentArgs);
            BootServiceManager.startBootServices();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }

        // TODO Extension

        // 安全销毁所有服务扩展点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                BootServiceManager.destroyBootServices();
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }));
    }
}
