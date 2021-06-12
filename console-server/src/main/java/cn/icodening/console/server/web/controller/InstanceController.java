package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.annotation.WrapperResponse;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.service.InstanceConfigurationService;
import cn.icodening.console.server.service.InstanceService;
import cn.icodening.console.server.util.ConsoleResponse;
import cn.icodening.console.server.web.controller.base.SpecificationQueryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.05.24
 */
@RestController
@RequestMapping("/instance")
@WrapperResponse
public class InstanceController implements SpecificationQueryController<InstanceEntity> {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private List<InstanceConfigurationService<?>> instanceConfigurationServices;

    @PostMapping("/register")
    public Object register(@RequestBody InstanceEntity instanceEntity) {
        InstanceEntity registered = instanceService.register(instanceEntity);
        Map<String, Object> map = new HashMap<>(8);
        for (InstanceConfigurationService<?> instanceConfigurationService : instanceConfigurationServices) {
            List<?> instanceConfiguration = instanceConfigurationService.findInstanceConfiguration(registered);
            map.put(instanceConfigurationService.configType(), instanceConfiguration);
        }
        return ConsoleResponse.ok(map);
    }

    @PostMapping("/deregister/{identity}")
    public Object deregister(@PathVariable String identity) {
        instanceService.deregister(identity);
        return ConsoleResponse.ok();
    }

    @Override
    public Specification<InstanceEntity> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params) {
        //TODO 实现各种条件的查询
        return (Specification<InstanceEntity>) (root, query, criteriaBuilder) -> query.getGroupRestriction();
    }

    @Override
    public IService<InstanceEntity> getService() {
        return instanceService;
    }
}
