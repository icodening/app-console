package cn.icodening.console.boot;

import cn.icodening.console.util.ExtensionClassLoaderHolder;

import java.lang.instrument.Instrumentation;
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

    public static void initBootServices(String args, Instrumentation instrumentation) {
        processBootServices(boot -> boot.initialize(args, instrumentation));

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
            List<BootService> bootServices = new ArrayList<>();
            iterator.forEachRemaining(bootServices::add);
            Collections.sort(bootServices);
            BootServiceManager.bootServices = bootServices;
        }
        return bootServices;
    }
}
