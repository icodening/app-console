package cn.icodening.console.register.spring;

import cn.icodening.console.model.PushData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ReceiveConfigServlet extends HttpServlet {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(ReceiveConfigServlet.class.getName() + ": receive message");
        try (ServletInputStream inputStream = req.getInputStream();
             ServletOutputStream outputStream = resp.getOutputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
            byte[] data = new byte[512];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                bos.write(data, 0, len);
            }
            System.out.println("receive message is: " + new String(bos.toByteArray()));
            final PushData pushData = objectMapper.readValue(bos.toByteArray(), PushData.class);
            applicationEventPublisher.publishEvent(new PushDataReceivedEvent(pushData));
            final HashMap<String, String> resultMap = new HashMap<>(2);
            resultMap.put("success", "true");
            resultMap.put("code", "200");
            final byte[] resultBytes = objectMapper.writeValueAsBytes(resultMap);
            outputStream.write(resultBytes);

        }
    }
}
