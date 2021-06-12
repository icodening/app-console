package cn.icodening.console.server.service.impl;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.server.repository.RateLimitRepository;
import cn.icodening.console.server.service.InstanceConfigurationService;
import cn.icodening.console.server.service.RateLimitService;
import cn.icodening.console.server.util.BeanPropertyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * FIXME 暂时不做各种参数的校验
 *
 * @author icodening
 * @date 2021.05.28
 */
@Service
public class RateLimitServiceImpl extends AbstractServiceImpl<RateLimitEntity, RateLimitRepository>
        implements RateLimitService, InstanceConfigurationService<RateLimitEntity> {

    @Override
    public void addRecord(RateLimitEntity record) {
        if (record.getEnable() == null) {
            record.setEnable(true);
        }
        record.setCreateTime(new Date());
        save(record);
    }


    @Override
    public void updateRecord(RateLimitEntity record) {
        Long id = record.getId();
        if (id == null) {
            throw new AppConsoleException("ID 不能为空");
        }
        RateLimitEntity recordFromDb = baseRepository.findById(id).orElseThrow(() -> new AppConsoleException("该记录不存在"));
        BeanUtils.copyProperties(record, recordFromDb, BeanPropertyUtil.getNullFieldNames(record));
        save(recordFromDb);
    }

    @Override
    public List<RateLimitEntity> findInstanceConfiguration(InstanceEntity instanceEntity) {
        if (instanceEntity == null) {
            return new ArrayList<>(Collections.emptyList());
        }
        String identity = instanceEntity.getIdentity();
        String applicationName = instanceEntity.getApplicationName();
        List<RateLimitEntity> instanceRateLimits = findBySpecification(new Specification<RateLimitEntity>() {
            @Override
            public Predicate toPredicate(Root<RateLimitEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate and1 = criteriaBuilder.and();

                Predicate equalApplicationScope = criteriaBuilder.equal(root.get("scope").as(String.class), "APPLICATION");
                Predicate equalApplicationName = criteriaBuilder.equal(root.get("affectTarget").as(String.class), applicationName);
                and1.getExpressions().add(equalApplicationScope);
                and1.getExpressions().add(equalApplicationName);


                Predicate and2 = criteriaBuilder.and();
                Predicate equalInstanceScope = criteriaBuilder.equal(root.get("scope").as(String.class), "INSTANCE");
                Predicate equalInstanceIdentity = criteriaBuilder.equal(root.get("affectTarget").as(String.class), identity);
                and2.getExpressions().add(equalInstanceScope);
                and2.getExpressions().add(equalInstanceIdentity);

                Predicate or = criteriaBuilder.or(and1, and2);
                query.where(or);
                return query.getRestriction();
            }
        });
        return instanceRateLimits;
    }

    @Override
    public String configType() {
        return RateLimitEntity.class.getName();
    }
}
