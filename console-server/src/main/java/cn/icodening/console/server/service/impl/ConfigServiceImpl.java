package cn.icodening.console.server.service.impl;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.common.entity.ConfigEntity;
import cn.icodening.console.common.entity.InstanceEntity;
import cn.icodening.console.server.repository.ConfigRepository;
import cn.icodening.console.server.service.ConfigService;
import cn.icodening.console.server.service.InstanceConfigurationService;
import cn.icodening.console.server.util.BeanPropertyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author icodening
 * @date 2021.07.10
 */
@Service
public class ConfigServiceImpl extends AbstractServiceImpl<ConfigEntity, ConfigRepository>
        implements ConfigService, InstanceConfigurationService<ConfigEntity> {

    @Override
    public void addRecord(ConfigEntity record) {
        try {
            new Properties().load(new StringReader(record.getContent()));
            record.setCreateTime(new Date());
            save(record);
        } catch (IOException e) {
            throw AppConsoleException.wrapperException(e);
        }
    }

    @Override
    public void updateRecord(ConfigEntity record) {
        try {
            Long id = record.getId();
            if (id == null) {
                throw new AppConsoleException("ID 不能为空");
            }
            new Properties().load(new StringReader(record.getContent()));
            ConfigEntity recordFromDb = baseRepository.findById(id).orElseThrow(() -> new AppConsoleException("该记录不存在"));
            BeanUtils.copyProperties(record, recordFromDb, BeanPropertyUtil.getNullFieldNames(record));
            save(recordFromDb);
        } catch (IOException e) {
            throw AppConsoleException.wrapperException(e);
        }
    }

    @Override
    public String configType() {
        return ConfigEntity.class.getName();
    }

    @Override
    public List<ConfigEntity> findInstanceConfiguration(InstanceEntity instanceEntity) {
        //FIXME 重复代码，后期优化
        if (instanceEntity == null) {
            return new ArrayList<>(Collections.emptyList());
        }
        String identity = instanceEntity.getIdentity();
        String applicationName = instanceEntity.getApplicationName();
        //查询限流规则配置中，根据"应用"范围生效的限流规则，或根据"实例"范围生效的限流规则
        return findBySpecification((Specification<ConfigEntity>) (root, query, criteriaBuilder) -> {
            Predicate equalApplicationScope = criteriaBuilder.equal(root.get("scope").as(String.class), "APPLICATION");
            Predicate equalApplicationName = criteriaBuilder.equal(root.get("affectTarget").as(String.class), applicationName);
            Predicate and1 = criteriaBuilder.and(equalApplicationScope, equalApplicationName);

            Predicate equalInstanceScope = criteriaBuilder.equal(root.get("scope").as(String.class), "INSTANCE");
            Predicate equalInstanceIdentity = criteriaBuilder.equal(root.get("affectTarget").as(String.class), identity);
            Predicate and2 = criteriaBuilder.and(equalInstanceScope, equalInstanceIdentity);

            Predicate or = criteriaBuilder.or(and1, and2);
            query.where(or);
            return query.getRestriction();
        });
    }
}
