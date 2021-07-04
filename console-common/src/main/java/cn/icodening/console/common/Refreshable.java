package cn.icodening.console.common;

import cn.icodening.console.common.model.ServerMessage;

/**
 * @author icodening
 * @date 2021.06.11
 */
public interface Refreshable {

    /**
     * refresh
     */
    void refresh(ServerMessage serverMessage);
}
