package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.service.RateLimitService;
import cn.icodening.console.server.web.controller.base.CrudController;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author icodening
 * @date 2021.06.07
 */
@RestController
@RequestMapping("/ratelimit")
public class RateLimitController implements CrudController<RateLimitEntity> {

    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public IService<RateLimitEntity> getService() {
        return rateLimitService;
    }

    @Override
    public Specification<RateLimitEntity> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params) {
        return (Specification<RateLimitEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String keywords = params.getFirst("keywords");
            if (StringUtils.hasText(keywords)) {
                Predicate and = Arrays.stream(keywords.split(" "))
                        .flatMap(kw -> {
                            Predicate or = criteriaBuilder.or();
                            List<Expression<Boolean>> expressions = or.getExpressions();
                            expressions.add(criteriaBuilder.like(root.get("scope").as(String.class), "%" + kw + "%"));
                            expressions.add(criteriaBuilder.like(root.get("affectTarget").as(String.class), "%" + kw + "%"));
                            expressions.add(criteriaBuilder.like(root.get("endpoint").as(String.class), "%" + kw + "%"));
                            return Stream.of(or);
                        }).reduce(criteriaBuilder.and(), (predicate, predicate2) -> {
                            predicate.getExpressions().add(predicate2);
                            return predicate;
                        });
                predicates.add(and);
            }
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }
}
