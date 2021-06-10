package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.util.ConsoleResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface DeleteEntityController<T> extends BaseController<T> {

    @DeleteMapping("/{id}")
    default ConsoleResponse deleteById(@PathVariable Long id) {
        getService().deleteById(id);
        return ConsoleResponse.ok();
    }

    @DeleteMapping
    default Object delete(@RequestBody Long[] ids) {
        getService().deleteByIds(Arrays.asList(ids));
        return ConsoleResponse.ok();
    }
}
