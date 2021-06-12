package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.http.Request;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.HttpUtil;
import com.alibaba.fastjson.JSON;

import java.io.IOException;

/**
 * 注册启动点
 *
 * @author icodening
 * @date 2021.05.24
 */
public class RegisterBootService extends BaseBootService {

    private volatile ApplicationInstance applicationInstance;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBootService.class);

    @Override
    public void start() throws AppConsoleException {
        String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
        EventDispatcher.register(ApplicationInstanceStartedEvent.class, (ConsoleEventListener<ApplicationInstanceStartedEvent>) event -> {
            ApplicationInstance applicationInstance = event.getApplicationInstance();
            if (applicationInstance != null) {
                RegisterBootService.this.applicationInstance = applicationInstance;
                Request post = Request.of(serverAddress + "/instance/register", "POST");
                post.getHeaders().set("Content-Type", "application/json;charset=utf8");
                byte[] requestBody = JSON.toJSONBytes(applicationInstance);
                post.setBody(requestBody);
                try {
                    HttpUtil.exchange(post);
                    LOGGER.info("register backend success! backend address is:" + serverAddress + "[" + applicationInstance + "]");
                } catch (IOException e) {
                    //TODO ignore or retry?
                    LOGGER.warn("register failed !!! [" + applicationInstance + "]", e.getCause());
                }
            }
        });
    }

    @Override
    public void destroy() {
        if (applicationInstance != null) {
            String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
            String identity = applicationInstance.getIdentity();
            Request post = Request.of(serverAddress + "/instance/deregister/" + identity, "POST");
            try {
                HttpUtil.exchange(post);
            } catch (IOException e) {
                throw AppConsoleException.wrapperException(e);
            }
        }
    }
}
