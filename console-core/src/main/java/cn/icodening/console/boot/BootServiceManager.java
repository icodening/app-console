package cn.icodening.console.boot;

import cn.icodening.console.util.ExtensionClassLoaderHolder;

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

    public static void initBootServices(String args) {
        processBootServices(boot -> boot.initialize(args));

    }

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
            ServiceLoader<BootService> load = ServiceLoader.load(BootService.class, ExtensionClassLoaderHolder.get());
            Iterator<BootService> iterator = load.iterator();
            ArrayList<BootService> bootServices = new ArrayList<>();
            iterator.forEachRemaining(bootServices::add);
            BootServiceManager.bootServices = bootServices;
            Collections.sort(BootServiceManager.bootServices);
        }
        return bootServices;
    }
}
