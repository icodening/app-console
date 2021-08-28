package cn.icodening.console.boot;

import cn.icodening.console.util.ServiceLoaderUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 启动点管理器
 *
 * @author icodening
 * @date 2021.05.21
 */
public class BootServiceManager {

    private static volatile List<BootService> bootServices = null;

    public static void startBootServices() {
        processBootServices(BootService::start);

    }

    public static void destroyBootServices() {
        processBootServices(BootService::destroy);
    }

    private static void processBootServices(Consumer<BootService> serviceConsumer) {
        List<BootService> bootServices = getBootServices();
        for (BootService bootService : bootServices) {
            serviceConsumer.accept(bootService);
        }
    }

    private static List<BootService> getBootServices() {
        if (null == bootServices) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            List<BootService> bootServices = ServiceLoaderUtil.getExtensions(BootService.class, contextClassLoader);
            Collections.sort(bootServices);
            BootServiceManager.bootServices = bootServices;
        }
        return bootServices;
    }
}
