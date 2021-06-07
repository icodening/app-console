package cn.icodening.console.server.web.controller.base;

import cn.icodening.console.server.service.IService;

/**
 * @author icodening
 * @date 2021.06.07
 */
public interface BaseController<T> {

    IService<T> getService();
}
