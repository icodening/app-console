package cn.icodening.console.agent;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.boot.BootServiceManager;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.AgentStartHelper;
import cn.icodening.console.util.ExtensionClassLoaderHolder;

import java.lang.instrument.Instrumentation;
import java.util.ServiceLoader;

/**
 * @author icodening
 * @date 2021.05.20
 */
public class AppConsoleAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConsoleAgent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        agentInitializer(agentArgs, instrumentation);
        AgentStartHelper.setStart(() -> startAgent(agentArgs, instrumentation));
    }

    private static void agentInitializer(String agentArgs, Instrumentation instrumentation) {
        try {
            ServiceLoader<AgentInitializer> load = ServiceLoader.load(AgentInitializer.class, ExtensionClassLoaderHolder.get());
            for (AgentInitializer agentInitializer : load) {
                agentInitializer.initialize(agentArgs, instrumentation);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private static void startAgent(String agentArgs, Instrumentation instrumentation) {
        LOGGER.info("app console agent start, version is v" + AppConsoleAgent.class.getPackage().getImplementationVersion());
        // 启动所有服务扩展点
        try {
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
