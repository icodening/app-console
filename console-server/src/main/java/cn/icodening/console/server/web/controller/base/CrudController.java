package cn.icodening.console.server.web.controller.base;

/**
 * CRUD通用Controller
 *
 * @author icodening
 * @date 2021.06.07
 */
public interface CrudController<T> extends AddRecordController<T>,
        DeleteEntityController<T>,
        UpdateRecordController<T>,
        SpecificationQueryController<T> {
}
