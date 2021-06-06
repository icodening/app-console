package cn.icodening.console.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.05.23
 */
class MessageListeners {

    private final List<MessageListener<?>> listeners = new ArrayList<>();

    MessageListeners() {
    }

    public void addListener(MessageListener messageListener) {
        listeners.add(messageListener);
    }

    public void removeListener(MessageListener messageListener) {
        listeners.remove(messageListener);
    }

    public void callListeners(Message message) {
        for (MessageListener<?> listener : listeners) {
            listener.onMessage(message);
        }
    }
}
