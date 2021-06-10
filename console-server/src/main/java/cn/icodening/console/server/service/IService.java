package cn.icodening.console.server.service;

import cn.icodening.console.server.util.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author icodening
 * @date 2021.05.24
 */
public interface IService<T> {

    long count(Specification<T> specification);

    long count();

    List<T> findAll();

    List<T> findBySpecification(Specification<T> specification);

    List<T> findBySpecification(Specification<T> specification, Pageable pageable);

    PageResult<T> findPage(Specification<T> specification);

    PageResult<T> findPage(Specification<T> specification, Pageable pageable);

    void save(T entity);

    void addRecord(T record);

    void updateRecord(T record);

    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    T findById(Long id);
}
