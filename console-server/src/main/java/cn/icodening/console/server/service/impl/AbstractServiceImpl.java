package cn.icodening.console.server.service.impl;

import cn.icodening.console.server.repository.BaseRepository;
import cn.icodening.console.server.service.IService;
import cn.icodening.console.server.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractServiceImpl<T, R extends BaseRepository<T>> implements IService<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected R baseRepository;

    @Autowired(required = false)
    public void setBaseRepository(R baseRepository) {
        this.baseRepository = baseRepository;
    }

    protected BaseRepository<T> getBaseRepository() {
        return this.baseRepository;
    }

    @Override
    public long count(Specification<T> specification) {
        if (specification == null) {
            return getBaseRepository().count();
        }
        return getBaseRepository().count(specification);
    }

    @Override
    public long count() {
        return count(null);
    }

    @Override
    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    @Override
    public List<T> findBySpecification(Specification<T> specification) {
        return getBaseRepository().findAll(specification);
    }

    @Override
    public List<T> findBySpecification(Specification<T> specification, Pageable pageable) {
        Page<T> page = getBaseRepository().findAll(specification, pageable);
        return page.getContent();
    }

    @Override
    public PageResult<T> findPage(Specification<T> specification) {
        if (specification == null) {
            return PageResult.createPageResult(findAll());
        }
        List<T> all = getBaseRepository().findAll(specification);
        return PageResult.createPageResult(all);
    }

    @Override
    public PageResult<T> findPage(Specification<T> specification, Pageable pageable) {
        if (pageable == null) {
            return findPage(specification);
        }
        Page<T> all = getBaseRepository().findAll(specification, pageable);
        return PageResult.createPageResult(all);
    }

    @Override
    public void save(T entity) {
        getBaseRepository().save(entity);
    }

    @Override
    public void addRecord(T record) {

    }

    @Override
    public void updateRecord(T record) {

    }

    @Override
    public void deleteById(Long id) {
        try {
            getBaseRepository().deleteById(id);
        } catch (EmptyResultDataAccessException ignore) {
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Override
    public T findById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<T> optionalEntity = getBaseRepository().findById(id);
        if (Optional.empty().equals(optionalEntity) || !optionalEntity.isPresent()) {
            return null;
        }
        return optionalEntity.get();
    }
}
