package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.common.util.ConsoleResponse;
import cn.icodening.console.server.common.util.PageResult;
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

    Specification<T> createSpecification(Integer currentPage, Integer pageSize, MultiValueMap<String, String> params);

    default void afterQuery(PageResult<T> page) {
    }

}
