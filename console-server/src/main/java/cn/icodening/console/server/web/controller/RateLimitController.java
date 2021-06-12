package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.service.RateLimitService;
import cn.icodening.console.server.web.controller.base.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author icodening
 * @date 2021.06.07
 */
@RestController
@RequestMapping("/ratelimit")
public class RateLimitController implements CrudController<RateLimitEntity> {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public IService<RateLimitEntity> getService() {
        return rateLimitService;
    }

    @Override
    public Specification<RateLimitEntity> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params) {
        return (Specification<RateLimitEntity>) (root, query, criteriaBuilder) -> query.getGroupRestriction();
    }
}
