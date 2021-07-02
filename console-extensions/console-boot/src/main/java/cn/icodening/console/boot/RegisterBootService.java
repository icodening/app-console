package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.http.Request;
import cn.icodening.console.http.Response;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

    private ObjectMapper objectMapper;

    @Override
    public void initialize(String agentArgs) {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void start() throws AppConsoleException {
        //FIXME magic number
        String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
        EventDispatcher.registerOnceEvent(ApplicationInstanceStartedEvent.class, (ConsoleEventListener<ApplicationInstanceStartedEvent>) event -> {
            ApplicationInstance applicationInstance = event.getApplicationInstance();
            if (applicationInstance != null) {
                RegisterBootService.this.applicationInstance = applicationInstance;
                Request post = Request.of(serverAddress + "/instance/register", "POST");
                post.getHeaders().set("Content-Type", "application/json;charset=utf8");
                try {
                    byte[] requestBody = objectMapper.writeValueAsBytes(applicationInstance);
                    post.setBody(requestBody);
                    Response exchange = HttpUtil.exchange(post);
                    //实例注册接口响应内容为当前注册实例中所有的配置信息.
                    //其中 "data" 的数据结构为一个map， config class -> config list
                    //例子 {"data":{"cn.icodening.Config1":[],"cn.icodening.Config2":[]}, "success": true}
                    byte[] dataBytes = exchange.getData();
                    JsonNode jsonNode = objectMapper.readTree(dataBytes);
                    JsonNode dataNode = jsonNode.get("data");
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
                    dataNode.fields().forEachRemaining(mapEntry -> {
                        try {
                            String key = mapEntry.getKey();
                            JsonNode arrayNode = mapEntry.getValue();
                            Class clz = Class.forName(key, false, contextClassLoader);
                            List<Object> configEntitys = new ArrayList<>();

                            for (int i = 0; arrayNode.has(i); i++) {
                                JsonNode objectNode = arrayNode.get(i);
                                Object target = objectMapper.readValue(objectNode.toString(), clz);
                                configEntitys.add(target);
                            }
                            ServerMessage serverMessage = new ServerMessage();
                            serverMessage.setType(clz.getName());
                            serverMessage.setSendTimestamp(System.currentTimeMillis());

                            InstanceConfigurationCache.setConfigs(clz, configEntitys);
                            ServerMessageReceivedEvent serverMessageReceivedEvent = new ServerMessageReceivedEvent(serverMessage);
                            EventDispatcher.dispatch(serverMessageReceivedEvent);
                        } catch (Exception e) {
                            LOGGER.warn("refresh instance configurations fail !!! ", e.getCause());
                        }
                    });
                    LOGGER.info("register backend success! backend address is:" + serverAddress + ". " + applicationInstance);
                } catch (IOException e) {
                    //TODO ignore or retry?
                    LOGGER.warn("register failed !!! " + applicationInstance, e.getCause());
                }
            }
        });
    }

    @Override
    public void destroy() {
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
