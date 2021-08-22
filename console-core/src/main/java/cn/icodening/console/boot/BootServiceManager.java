package cn.icodening.console.boot;

import java.util.*;
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
            ServiceLoader<BootService> boots = ServiceLoader.load(BootService.class, contextClassLoader);
            Iterator<BootService> iterator = boots.iterator();
            List<BootService> bootServices = new ArrayList<>();
            iterator.forEachRemaining(bootServices::add);
            Collections.sort(bootServices);
            BootServiceManager.bootServices = bootServices;
        }
        return bootServices;
    }
}
