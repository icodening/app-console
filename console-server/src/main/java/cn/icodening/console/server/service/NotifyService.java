package cn.icodening.console.server.service;

import cn.icodening.console.server.model.PushData;

import java.util.List;

/**
 * 推送通知服务
 *
 * @author icodening
 * @date 2021.06.08
 */
public interface NotifyService {

    /**
     * 推送通知
     *
     * @param pushData 推送的数据包
     * @param address  需要推送的实例地址
     */
    void notify(PushData pushData, List<String> address);
}
