package cn.icodening.console.register.spring;

import cn.icodening.console.common.convert.ClassCasterManager;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.EventDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ReceiveConfigServlet extends HttpServlet {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    @SuppressWarnings("unchecked")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(ReceiveConfigServlet.class.getName() + ": receive message from: " + req.getRemoteAddr());
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
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(type);

            ArrayList arrayList = objectMapper.readValue(bos.toByteArray(), ArrayList.class);
            //FIXME extract common method or util
            List retList = new ArrayList<>();
            for (Object o : arrayList) {
                if (o instanceof Map) {
                    Map cast = Map.class.cast(o);
                    Object target = type.newInstance();
                    System.out.println(cast);
                    retList.add(target);
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        String name = propertyDescriptor.getName();
                        if ("class".equals(name)) {
                            continue;
                        }
                        Object value = cast.get(name);
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        Class<?> propertyType = propertyDescriptor.getPropertyType();
                        value = ClassCasterManager.cast(value, propertyType);
                        if (writeMethod != null) {
                            writeMethod.invoke(target, value);
                        }
                    }
                }
            }
            InstanceConfigurationCache.setConfigs(type, retList);
            ServerMessage serverMessage = new ServerMessage();
            serverMessage.setType(messageType);
            CompletableFuture.runAsync(() -> EventDispatcher.dispatch(new ServerMessageReceivedEvent(serverMessage)))
                    .whenCompleteAsync((ret, ex) -> System.out.println("publish server message received event success"));
            outputStream.write("success".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
