package cn.icodening.console.agent;

import cn.icodening.console.boot.BootService;
import cn.icodening.console.extension.ExtensionLoader;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author icodening
 * @date 2021.05.20
 */
public class AppConsoleAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("app console agent start");
        List<BootService> bootServices = ExtensionLoader.getExtensionLoader(BootService.class).getAllExtension();
        for (BootService bootService : bootServices) {
            bootService.initialize();
            bootService.start();
        }
    }
}
