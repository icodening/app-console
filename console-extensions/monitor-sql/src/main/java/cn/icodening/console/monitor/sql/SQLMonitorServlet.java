package cn.icodening.console.monitor.sql;

import cn.icodening.console.common.vo.SQLMonitorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * FIXME 优化传输超大报文
 *
 * @author icodening
 * @date 2021.08.01
 */
public class SQLMonitorServlet extends FrameworkServlet {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SQLMonitorData> allMonitorData = SQLMonitorDataRepository.getAllMonitorData();
        byte[] bytes = objectMapper.writeValueAsBytes(allMonitorData);
        try (OutputStream out = response.getOutputStream();) {
            out.write(bytes);
            out.flush();
        }
    }
}
