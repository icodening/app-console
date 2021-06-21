package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.util.ConsoleResponse;
import cn.icodening.console.server.util.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface SpecificationQueryController<T> extends BaseController<T> {

    /**
     * 查询
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param params      查询参数
     * @return 查询结果
     */
    @GetMapping
    default Object query(@RequestParam(defaultValue = "1") Integer currentPage,
                         Integer pageSize,
                         @RequestParam MultiValueMap<String, String> params) {
        Specification<T> specification = createSpecification(currentPage, pageSize, params);
        PageRequest pageRequest = null;
        if (pageSize != null && pageSize > 0) {
            pageRequest = PageRequest.of(currentPage - 1, pageSize);
        }
        PageResult<T> page = getService().findPage(specification, pageRequest);
        afterQuery(page);
        return ConsoleResponse.ok(page);
    }

    /**
     * 构造查询条件
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param params      查询参数
     * @return 查询条件体
     */
    Specification<T> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params);

    /**
     * 查询后置处理
     *
     * @param page 当前查询结果集
     */
    default void afterQuery(PageResult<T> page) {
    }

}
