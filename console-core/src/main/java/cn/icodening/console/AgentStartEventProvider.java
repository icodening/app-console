package cn.icodening.console;

import cn.icodening.console.event.AgentStartEvent;

/**
 * use {@link java.util.ServiceLoader}
 * agent启动事件获取器
 * TODO 按照当前设计，该类应该是不需要了，考虑删除
 *
 * @author icodening
 * @date 2021.06.27
 */
@Deprecated
public interface AgentStartEventProvider extends Sortable {

    /**
     * 获取agent启动事件
     *
     * @return agent start event
     */
    AgentStartEvent get();
}
