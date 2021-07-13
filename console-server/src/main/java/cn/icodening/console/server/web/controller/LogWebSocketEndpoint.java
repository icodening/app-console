package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.service.InstanceService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.annotation.Resource;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author icodening
 * @date 2021.07.11
 */
@ServerEndpoint(value = "/log/{instanceId}", configurator = SpringConfigurator.class)
@Component
public class LogWebSocketEndpoint {

    @Resource
    private InstanceService instanceService;

    @OnOpen
    public void onOpen(Session session, @PathParam("instanceId") Long instanceId) {
        InstanceEntity instance = instanceService.findById(instanceId);
        if (instance != null) {
            WebSocketContainer webSocketContainer = session.getContainer();
            String url = "ws://" + instance.getIp() + ":" + instance.getPort() + "/log";
            StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient(webSocketContainer);
            standardWebSocketClient.doHandshake(new BinaryWebSocketHandler() {
                @Override
                protected void handleBinaryMessage(WebSocketSession webSocketSession, BinaryMessage message) throws Exception {
                    if (!session.isOpen()) {
                        TextMessage textMessage = new TextMessage("close");
                        webSocketSession.sendMessage(textMessage);
                        return;
                    }
                    session.getBasicRemote().sendBinary(message.getPayload());
                }
            }, url);
        }
    }
}
