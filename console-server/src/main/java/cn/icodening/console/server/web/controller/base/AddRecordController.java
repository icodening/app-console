package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.util.ConsoleResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface AddRecordController<T> extends BaseController<T> {

    @PostMapping
    default Object addRecord(@RequestBody T record) {
        getService().addRecord(record);
        return ConsoleResponse.ok();
    }
}
