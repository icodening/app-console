package cn.icodening.console.listener;

/**
 * @author icodening
 * @date 2021.05.23
 */
@FunctionalInterface
public interface MessageListener<T> {


    /**
     * 当消息到来触发
     *
     * @param message 收到的消息
     */
    void onMessage(Message<T> message);
}
