package cn.icodening.console.server.web.controller;

import cn.icodening.console.server.entity.RateLimitEntity;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.service.RateLimitService;
import cn.icodening.console.server.web.controller.base.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        return new Specification<RateLimitEntity>() {
            @Override
            public Predicate toPredicate(Root<RateLimitEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.getGroupRestriction();
            }
        };
    }
}
