package cn.icodening.console.server.service.impl;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.service.InstanceFinder;
import cn.icodening.console.server.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 根据实例标识寻找实例信息
 *
 * @author icodening
 * @date 2021.06.08
 */
@Component
public class IdentityInstanceFinder implements InstanceFinder {

    @Autowired
    private InstanceService instanceService;

    @Override
    public String classify() {
        return "INSTANCE";
    }

    @Override
    public List<InstanceEntity> find(String identity) {
        return instanceService.findBySpecification((Specification<InstanceEntity>)
                (root, query, criteriaBuilder) -> {
                    Predicate equal = criteriaBuilder.equal(root.get("identity").as(String.class), identity);
                    query.where(equal);
                    return query.getRestriction();
                });
    }
}
