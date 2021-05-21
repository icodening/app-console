package cn.icodening.console.agent;

import cn.icodening.console.boot.BootServiceManager;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.05.20
 */
public class AppConsoleAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        //FIXME LOG
        System.out.println("app console agent start");
        // 启动所有服务扩展点
        try {
            BootServiceManager.initBootServices(agentArgs);
            BootServiceManager.startBootServices();
        } catch (Exception e) {
            //FIXME LOG
            System.out.println(e.getMessage());
        }

        // TODO Extension

        // 安全销毁所有服务扩展点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                BootServiceManager.destroyBootServices();
            } catch (Exception e) {
                //FIXME LOG
                System.out.println(e.getMessage());
            }
        }));
    }
}
