package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.util.ConsoleResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface UpdateRecordController<T> extends BaseController<T> {

    @PutMapping
    default Object updateRecord(@RequestBody T record) {
        getService().updateRecord(record);
        return ConsoleResponse.ok();
    }
}
