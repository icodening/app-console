package cn.icodening.console.web.controller;

import cn.icodening.console.annotation.WrapperResponse;
import cn.icodening.console.entity.InstanceEntity;
import cn.icodening.console.service.InstanceService;
import cn.icodening.console.util.ConsoleResponse;
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

    @DeleteMapping("/deregister")
    public Object deregister() {
        return "success";
    }
}
