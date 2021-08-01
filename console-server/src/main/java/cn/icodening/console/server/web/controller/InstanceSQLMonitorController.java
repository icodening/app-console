package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.constants.URLConstants;
import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.common.vo.SQLMonitorData;
import cn.icodening.console.server.annotation.WrapperResponse;
import cn.icodening.console.server.service.InstanceService;
import cn.icodening.console.server.util.PageResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author icodening
 * @date 2021.08.01
 */
@RestController
@RequestMapping("/sqlMonitor")
@WrapperResponse
public class InstanceSQLMonitorController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InstanceService instanceService;

    @GetMapping("/{instanceId}")
    public Object querySqlMonitor(@PathVariable(name = "instanceId") Long instanceId) throws IOException {
        InstanceEntity instanceEntity = instanceService.findById(instanceId);
        String ip = instanceEntity.getIp();
        Integer port = instanceEntity.getPort();
        String address = "http://" + ip + ":" + port + URLConstants.INSTANCE_SQLMONITOR_URL;
        byte[] forObject = restTemplate.getForObject(address, byte[].class);
        List<SQLMonitorData> sqlMonitorDatas = objectMapper.readValue(forObject, new TypeReference<List<SQLMonitorData>>() {
        });
        Collections.reverse(sqlMonitorDatas);
        return PageResult.createPageResult(sqlMonitorDatas);
    }

}
