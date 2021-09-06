package cn.icodening.console.register.spring;

import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.FrameworkServlet;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ReceiveConfigServlet extends FrameworkServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveConfigServlet.class);

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ThreadPoolTaskExecutor dispatcherExecutor;

    @Override
    @SuppressWarnings("unchecked")
    protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        LOGGER.debug("receive message from: " + req.getRemoteAddr());
        try (ServletInputStream inputStream = req.getInputStream()) {
            MimeType mimeType = MimeTypeUtils.parseMimeType(req.getContentType());
            Charset charset = mimeType.getCharset();
            String charsetEncoding = mimeType.getCharset() != null ? charset.name() : Charset.defaultCharset().name();
            String dataString = StreamUtils.copyToString(inputStream, Charset.forName(charsetEncoding));
            LOGGER.debug("receive message data: \n" + dataString);
            String messageType = req.getHeader("Console-Push-Type");
            String messageAction = req.getHeader("Console-Push-Action");
            Class type = Class.forName(messageType);
            List<Object> retList = convertToConfigList(dataString, type);
            List<Object> config = InstanceConfigurationCache.getConfigs(type);
            Set<Object> configSet = new HashSet<>(retList);
            if (config != null) {
                configSet.addAll(config);
            }
            retList = new ArrayList<>(configSet);
            InstanceConfigurationCache.setConfigs(type, retList);
            ServerMessage serverMessage = new ServerMessage();
            serverMessage.setType(messageType);
            serverMessage.setAction(messageAction);
            CompletableFuture.runAsync(() -> EventDispatcher.dispatch(new ServerMessageReceivedEvent(serverMessage)), dispatcherExecutor)
                    .whenCompleteAsync((ret, ex) -> LOGGER.debug("publish server message received event success"));
        } catch (Exception e) {
            LOGGER.warn("process message error, beacause: " + e.getMessage(), e);
        }
    }

    private <T> List<T> convertToConfigList(String data, Class<T> clazz) throws IOException {
        JsonNode arrayNode = objectMapper.readTree(data);
        List<T> retList = new ArrayList<>();
        for (int index = 0; arrayNode.has(index); index++) {
            JsonNode objectNode = arrayNode.get(index);
            Object target = objectMapper.readValue(objectNode.toString(), clazz);
            retList.add((T) target);
        }
        return retList;
    }
}
