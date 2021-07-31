package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.annotation.WrapperResponse;
import cn.icodening.console.server.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private InstanceService instanceService;

    @GetMapping("/{instanceId}")
    public Object querySqlMonitor(@PathVariable(name = "instanceId") Long instanceId) {
        InstanceEntity instanceEntity = instanceService.findById(instanceId);
        String ip = instanceEntity.getIp();
        Integer port = instanceEntity.getPort();
        String address = "http://" + ip + ":" + port + "/sqlMonitor";
        return null;
    }

}
