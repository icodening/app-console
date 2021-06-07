package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.common.util.ConsoleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface FindOneByIdController<T> extends BaseController<T> {

    @GetMapping("/{id}")
    default Object getOne(@PathVariable Long id) {
        T entity = getService().findById(id);
        return ConsoleResponse.ok(entity);
    }
}
