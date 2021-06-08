package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.http.Request;
import cn.icodening.console.model.ApplicationInstance;
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

    @Override
    public void start() throws AppConsoleException {
        String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
        //TODO register callback for application ready
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
                } catch (IOException e) {
                    //TODO LOG ignore register fail
                    e.printStackTrace();
                }
            }
        });
        System.out.println(RegisterBootService.class.getName() + ": add register callback success");
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
                //FIXME LOG
                e.printStackTrace();
            }
        }
    }
}
