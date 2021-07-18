package cn.icodening.console.server.service.impl;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.server.repository.RouterConfigRepository;
import cn.icodening.console.server.service.InstanceConfigurationService;
import cn.icodening.console.server.service.RouterConfigService;
import cn.icodening.console.server.util.BeanPropertyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.18
 */
@Service
public class RouterConfigServiceImpl extends AbstractServiceImpl<RouterConfigEntity, RouterConfigRepository>
        implements RouterConfigService, InstanceConfigurationService<RouterConfigEntity> {

    @Override
    public void addRecord(RouterConfigEntity record) {
        if (record.getEnable() == null) {
            record.setEnable(true);
        }
        record.setCreateTime(new Date());
        save(record);
    }


    @Override
    public void updateRecord(RouterConfigEntity record) {
        Long id = record.getId();
        if (id == null) {
            throw new AppConsoleException("ID 不能为空");
        }
        RouterConfigEntity recordFromDb = baseRepository.findById(id).orElseThrow(() -> new AppConsoleException("该记录不存在"));
        BeanUtils.copyProperties(record, recordFromDb, BeanPropertyUtil.getNullFieldNames(record));
        save(recordFromDb);
    }

    @Override
    public List<RouterConfigEntity> findInstanceConfiguration(InstanceEntity instanceEntity) {
        if (instanceEntity == null) {
            return new ArrayList<>(Collections.emptyList());
        }
        String identity = instanceEntity.getIdentity();
        String applicationName = instanceEntity.getApplicationName();
        //查询限流规则配置中，根据"应用"范围生效的限流规则，或根据"实例"范围生效的限流规则
        return findBySpecification((Specification<RouterConfigEntity>) (root, query, criteriaBuilder) -> {
            //FIXME 重复代码
            Predicate equalApplicationScope = criteriaBuilder.equal(root.get("scope").as(String.class), "APPLICATION");
            Predicate equalApplicationName = criteriaBuilder.equal(root.get("affectTarget").as(String.class), applicationName);
            Predicate and1 = criteriaBuilder.and(equalApplicationScope, equalApplicationName);

            Predicate equalInstanceScope = criteriaBuilder.equal(root.get("scope").as(String.class), "INSTANCE");
            Predicate equalInstanceIdentity = criteriaBuilder.equal(root.get("affectTarget").as(String.class), identity);
            Predicate and2 = criteriaBuilder.and(equalInstanceScope, equalInstanceIdentity);

            Predicate or = criteriaBuilder.or(and1, and2);

            Predicate isEnable = criteriaBuilder.equal(root.get("enable").as(Boolean.class), Boolean.TRUE);
            Predicate where = criteriaBuilder.and(or, isEnable);

            query.where(where);
            return query.getRestriction();
        });
    }

    @Override
    public String configType() {
        return RouterConfigEntity.class.getName();
    }
}
