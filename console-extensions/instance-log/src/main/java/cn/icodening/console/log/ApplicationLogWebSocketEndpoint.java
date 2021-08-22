package cn.icodening.console.log;

import cn.icodening.console.config.ConfigurationManager;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.07.12
 */
@ServerEndpoint(value = "/log", configurator = SpringWebSocketConfigurator.class)
public class ApplicationLogWebSocketEndpoint {

    private static final Map<String, BytesConsumer> LOG_BYTES_FLUSH_CONSUMER_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(ConfigurationManager.INSTANCE.get(InstanceLogInitializer.TEMP_LOG_PATH_KEY)));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        final RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
        for (String line; (line = bufferedReader.readLine()) != null; ) {
            line += "\n";
            byte[] lineBytes = line.getBytes();
            WebSocketUtil.fragmentTransmission(session, lineBytes);
        }
        BytesConsumer logBytesFlushConsumer = LOG_BYTES_FLUSH_CONSUMER_MAP.get(session.getId());
        if (logBytesFlushConsumer == null) {
            logBytesFlushConsumer = new WebSocketLogPushBytesConsumer(session);
            LOG_BYTES_FLUSH_CONSUMER_MAP.putIfAbsent(session.getId(), logBytesFlushConsumer);
            SystemPrintStreamDecorator systemPrintStreamDecorator = SystemOutputStreamHolder.getSystemPrintStreamDecorator();
            systemPrintStreamDecorator.registerFlushCallback(logBytesFlushConsumer);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String id = session.getId();
        BytesConsumer bytesConsumer = LOG_BYTES_FLUSH_CONSUMER_MAP.remove(id);
        if (bytesConsumer != null) {
            SystemOutputStreamHolder.getSystemPrintStreamDecorator().deregisterFlushCallback(bytesConsumer);
        }
    }
}
