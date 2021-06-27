package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.common.util.BeanMapUtil;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.http.Request;
import cn.icodening.console.http.Response;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        EventDispatcher.registerOnceEvent(ApplicationInstanceStartedEvent.class, (ConsoleEventListener<ApplicationInstanceStartedEvent>) event -> {
            ApplicationInstance applicationInstance = event.getApplicationInstance();
            if (applicationInstance != null) {
                RegisterBootService.this.applicationInstance = applicationInstance;
                Request post = Request.of(serverAddress + "/instance/register", "POST");
                post.getHeaders().set("Content-Type", "application/json;charset=utf8");
                byte[] requestBody = JSON.toJSONBytes(applicationInstance);
                post.setBody(requestBody);
                try {
                    Response exchange = HttpUtil.exchange(post);
                    byte[] data = exchange.getData();
                    JSONObject json = JSON.parseObject(new String(data));
                    String dataJsonString = json.getString("data");
                    Map<String, List<Map<String, Object>>> stringListMap = JSON.parseObject(dataJsonString, new TypeReference<Map<String, List<Map<String, Object>>>>() {
                    });
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    stringListMap.forEach((key, value) -> {
                        try {
                            Class clz = Class.forName(key, false, contextClassLoader);
                            ServerMessage serverMessage = new ServerMessage();
                            serverMessage.setType(clz.getName());
                            serverMessage.setSendTimestamp(System.currentTimeMillis());
                            List<Object> objects = new ArrayList<>();
                            for (Map<String, Object> map : value) {
                                Object target = BeanMapUtil.mapToBean(map, clz);
                                objects.add(target);
                            }
                            InstanceConfigurationCache.setConfigs(clz, objects);
                            //FIXME setAction?
                            ServerMessageReceivedEvent serverMessageReceivedEvent = new ServerMessageReceivedEvent(serverMessage);
                            EventDispatcher.dispatch(serverMessageReceivedEvent);
                        } catch (Exception ignore) {
                            ignore.printStackTrace();
                        }
                    });
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
