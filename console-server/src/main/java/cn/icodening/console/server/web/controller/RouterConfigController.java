package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.service.RouterConfigService;
import cn.icodening.console.server.web.controller.base.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author icodening
 * @date 2021.07.18
 */
@RestController
@RequestMapping("/routerConfig")
public class RouterConfigController implements CrudController<RouterConfigEntity> {

    @Autowired
    private RouterConfigService routerConfigService;

    @Override
    public IService<RouterConfigEntity> getService() {
        return routerConfigService;
    }
}
