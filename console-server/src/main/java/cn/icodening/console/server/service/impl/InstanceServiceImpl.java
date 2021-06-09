package cn.icodening.console.server.service.impl;

import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.common.util.BeanPropertyUtil;
import cn.icodening.console.server.repository.InstanceRepository;
import cn.icodening.console.server.service.InstanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Date;

/**
 * @author icodening
 * @date 2021.05.28
 */
@Service
public class InstanceServiceImpl extends AbstractServiceImpl<InstanceEntity, InstanceRepository> implements InstanceService {

    @Override
    public void register(InstanceEntity instanceEntity) {
        InstanceEntity entity = baseRepository.findOne((Specification<InstanceEntity>)
                (root, query, criteriaBuilder) -> {
                    Predicate equal = criteriaBuilder.equal(root.get("identity").as(String.class), instanceEntity.getIdentity());
                    query.where(equal);
                    return query.getRestriction();
                }).orElseGet(() -> {
            instanceEntity.setCreateTime(new Date());
            return instanceEntity;
        });
        final String[] fieldNames = BeanPropertyUtil.getNullFieldNames(instanceEntity);
        BeanUtils.copyProperties(instanceEntity, entity, fieldNames);
        save(entity);
    }

    @Override
    public void deregister(String identity) {
        baseRepository.findOne((Specification<InstanceEntity>) (root, query, criteriaBuilder) -> {
            Predicate equal = criteriaBuilder.equal(root.get("identity").as(String.class), identity);
            query.where(equal);
            return query.getRestriction();
        }).ifPresent(entity -> baseRepository.deleteById(entity.getId()));
    }
}
