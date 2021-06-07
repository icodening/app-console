package cn.icodening.console.server.service.impl;

import cn.icodening.console.server.common.ex.ConsoleException;
import cn.icodening.console.server.entity.RateLimitEntity;
import cn.icodening.console.server.repository.RateLimitRepository;
import cn.icodening.console.server.service.RateLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * FIXME 暂时不做各种参数的校验
 *
 * @author icodening
 * @date 2021.05.28
 */
@Service
public class RateLimitServiceImpl extends AbstractServiceImpl<RateLimitEntity, RateLimitRepository> implements RateLimitService {

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
            throw new ConsoleException("ID 不能为空");
        }
        RateLimitEntity recordFromDb = baseRepository.findById(id).orElseThrow(() -> new ConsoleException("该记录不存在"));
        BeanUtils.copyProperties(record, recordFromDb, "createTime", "modifyTime", "id");
        save(recordFromDb);
    }
}
