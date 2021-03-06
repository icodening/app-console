package cn.icodening.console.server.web.controller;

import cn.icodening.console.common.entity.ConfigEntity;
import cn.icodening.console.server.service.ConfigService;
import cn.icodening.console.server.service.IService;
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
 * @date 2021.07.10
 */
@RestController
@RequestMapping("/config")
public class ConfigController implements CrudController<ConfigEntity> {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public IService<ConfigEntity> getService() {
        return configService;
    }

    @Override
    public Specification<ConfigEntity> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params) {
        return (Specification<ConfigEntity>) (root, query, criteriaBuilder) -> {
            //FIXME 代码重复率高 后续优化
            List<Predicate> predicates = new ArrayList<>();
            String keywords = params.getFirst("keywords");
            if (StringUtils.hasText(keywords)) {
                Predicate and = Arrays.stream(keywords.split(" "))
                        .flatMap(kw -> {
                            Predicate or = criteriaBuilder.or();
                            List<Expression<Boolean>> expressions = or.getExpressions();
                            expressions.add(criteriaBuilder.like(root.get("scope").as(String.class), "%" + kw + "%"));
                            expressions.add(criteriaBuilder.like(root.get("affectTarget").as(String.class), "%" + kw + "%"));
                            expressions.add(criteriaBuilder.like(root.get("content").as(String.class), "%" + kw + "%"));
                            return Stream.of(or);
                        }).reduce(criteriaBuilder.and(), (predicate, predicate2) -> {
                            predicate.getExpressions().add(predicate2);
                            return predicate;
                        });
                predicates.add(and);
            }
            String scope = params.getFirst("scope");
            if (StringUtils.hasText(scope)) {
                Predicate and = criteriaBuilder.and(criteriaBuilder.equal(root.get("scope").as(String.class), scope));
                predicates.add(and);
            }
            String affectTarget = params.getFirst("affectTarget");
            if (StringUtils.hasText(affectTarget)) {
                Predicate and = criteriaBuilder.and(criteriaBuilder.equal(root.get("affectTarget").as(String.class), affectTarget));
                predicates.add(and);
            }
            String enable = params.getFirst("enable");
            if (StringUtils.hasText(affectTarget)) {
                Predicate and = criteriaBuilder.and(criteriaBuilder.equal(root.get("enable").as(Boolean.class), Boolean.valueOf(enable)));
                predicates.add(and);
            }
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }
}
