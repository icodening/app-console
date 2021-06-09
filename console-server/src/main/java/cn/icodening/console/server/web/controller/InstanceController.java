package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.annotation.WrapperResponse;
import cn.icodening.console.server.common.util.ConsoleResponse;
import cn.icodening.console.server.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author icodening
 * @date 2021.05.24
 */
@RestController
@RequestMapping("/instance")
@WrapperResponse
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @PostMapping("/register")
    public Object register(@RequestBody InstanceEntity instanceEntity) {
        instanceService.register(instanceEntity);
        return ConsoleResponse.ok();
    }

    @PostMapping("/deregister/{identity}")
    public Object deregister(@PathVariable String identity) {
        instanceService.deregister(identity);
        return ConsoleResponse.ok();
    }
}
