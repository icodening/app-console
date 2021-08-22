package cn.icodening.console.common.util;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.http.Request;
import cn.icodening.console.util.HttpUtil;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author icodening
 * @date 2021.08.22
 */
public class ApplicationInstanceHelper {

    public static volatile ApplicationInstance applicationInstance;

    public static volatile Consumer<ApplicationInstance> consumer;

    public static volatile boolean isInit = false;

    public static void initialization(Consumer<ApplicationInstance> consumer) {
        if (consumer == null || isInit) {
            return;
        }
        ApplicationInstanceHelper.consumer = consumer;
        ApplicationInstanceHelper.isInit = true;
    }

    public static void register(ApplicationInstance applicationInstance) {
        if (applicationInstance == null || !isInit) {
            return;
        }
        consumer.accept(applicationInstance);
    }

    public static void destroy() {
        if (applicationInstance != null) {
            String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
            String identity = applicationInstance.getIdentity();
            //FIXME magic number
            Request post = Request.of(serverAddress + "/instance/deregister/" + identity, "POST");
            try {
                HttpUtil.exchange(post);
            } catch (IOException e) {
                throw AppConsoleException.wrapperException(e);
            }
        }
    }
}
