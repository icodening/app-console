package cn.icodening.console;

import cn.icodening.console.event.AgentStartEvent;

/**
 * use {@link java.util.ServiceLoader}
 * agent启动事件获取器
 *
 * @author icodening
 * @date 2021.06.27
 */
public interface AgentStartEventProvider extends Sortable {

    /**
     * 获取agent启动事件
     *
     * @return agent start event
     */
    AgentStartEvent get();
}
