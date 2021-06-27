package cn.icodening.console.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class EventDispatcher {

    private static final Map<Class<? extends AppConsoleEvent>, EventListenerInvoker>
            EVENT_LISTENER_INVOKERS = new ConcurrentHashMap<>();

    public static void register(Class<? extends AppConsoleEvent> eventClazz,
                                ConsoleEventListener<? extends AppConsoleEvent> eventListener) {
        register(eventClazz, eventListener, false);
    }

    public static void registerOnceEvent(Class<? extends AppConsoleEvent> eventClazz,
                                         ConsoleEventListener<? extends AppConsoleEvent> eventListener) {
        register(eventClazz, eventListener, true);
    }

    private static void register(Class<? extends AppConsoleEvent> eventClazz,
                                 ConsoleEventListener<? extends AppConsoleEvent> eventListener, boolean once) {
        EventListenerInvoker eventListenerInvoker = EVENT_LISTENER_INVOKERS.get(eventClazz);
        if (eventListenerInvoker == null) {
            EVENT_LISTENER_INVOKERS.putIfAbsent(eventClazz, new EventListenerInvoker());
            eventListenerInvoker = EVENT_LISTENER_INVOKERS.get(eventClazz);
            eventListenerInvoker.event = eventClazz;
            eventListenerInvoker.listeners = new CopyOnWriteArrayList<>();
            eventListenerInvoker.once = once;
        }
        eventListenerInvoker.listeners.add(eventListener);
    }

    public static void dispatch(AppConsoleEvent event) {
        if (event == null) {
            return;
        }
        Class<? extends AppConsoleEvent> eventClazz = event.getClass();
        EventListenerInvoker eventListenerInvoker = EVENT_LISTENER_INVOKERS.get(eventClazz);
        if (eventListenerInvoker == null || eventListenerInvoker.listeners == null) {
            return;
        }
        if (eventListenerInvoker.once) {
            EVENT_LISTENER_INVOKERS.remove(eventClazz);
        }
        for (ConsoleEventListener listener : eventListenerInvoker.listeners) {
            listener.onEvent(event);
        }
    }

    private static class EventListenerInvoker {
        private Class<? extends AppConsoleEvent> event;
        private List<ConsoleEventListener<? extends AppConsoleEvent>> listeners;
        private boolean once;
    }
}
