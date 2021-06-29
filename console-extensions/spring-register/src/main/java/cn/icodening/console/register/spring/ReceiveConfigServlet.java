package cn.icodening.console.register.spring;

import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.common.util.BeanMapUtil;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ReceiveConfigServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveConfigServlet.class);

    @Resource
    private ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("receive message from: " + req.getRemoteAddr());
        try (ServletInputStream inputStream = req.getInputStream();
             ServletOutputStream outputStream = resp.getOutputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
            String messageType = req.getHeader("Console-Push-Type");
            byte[] data = new byte[512];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                bos.write(data, 0, len);
            }
            Class type = Class.forName(messageType);
            ArrayList<Object> arrayList = objectMapper.readValue(bos.toByteArray(), ArrayList.class);
            //FIXME extract common method or util
            List<Object> retList = new ArrayList<>();
            for (Object o : arrayList) {
                if (o instanceof Map) {
                    Map<String, Object> cast = (Map<String, Object>) o;
                    Object target = BeanMapUtil.mapToBean(cast, type);
                    retList.add(target);
                }
            }
            InstanceConfigurationCache.setConfigs(type, retList);
            ServerMessage serverMessage = new ServerMessage();
            serverMessage.setType(messageType);
            CompletableFuture.runAsync(() -> EventDispatcher.dispatch(new ServerMessageReceivedEvent(serverMessage)))
                    .whenCompleteAsync((ret, ex) -> LOGGER.debug("publish server message received event success"));
            outputStream.write("success".getBytes());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
