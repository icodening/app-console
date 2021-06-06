package cn.icodening.console.listener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author icodening
 * @date 2021.05.23
 */
public class MessageDispatcher {

    private static final ConcurrentMap<String, MessageListeners> MESSAGE_LISTENER_CONCURRENT_MAP = new ConcurrentHashMap<>();

    private MessageDispatcher() {
    }

    public static void register(String type, MessageListener<?> messageListener) {
        if (type == null) {
            return;
        }
        String intern = type.intern();
        synchronized (intern) {
            MessageListeners messageListeners = MESSAGE_LISTENER_CONCURRENT_MAP.get(type);
            if (messageListener == null) {
                messageListeners = new MessageListeners();
                MESSAGE_LISTENER_CONCURRENT_MAP.put(type, messageListeners);
            }
            messageListeners.addListener(messageListener);
        }
    }

    public static void remove(String type, MessageListener<?> messageListener) {
        if (type == null) {
            return;
        }
        String intern = type.intern();
        synchronized (intern) {
            MessageListeners messageListeners = MESSAGE_LISTENER_CONCURRENT_MAP.get(type);
            if (messageListener == null) {
                return;
            }
            messageListeners.removeListener(messageListener);
        }
    }

    public static void dispatch(Message message) {
        if (message == null) {
            return;
        }
        String type = message.getType();
        if (type == null) {
            return;
        }
        String intern = type.intern();
        synchronized (intern) {
            MessageListeners messageListeners = MESSAGE_LISTENER_CONCURRENT_MAP.get(intern);
            if (messageListeners == null) {
                return;
            }
            messageListeners.callListeners(message);
        }
    }

}
