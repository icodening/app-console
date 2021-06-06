package cn.icodening.console.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class EventDispatcher {

    private static final Map<Class<? extends AppConsoleEvent>, List<ConsoleEventListener<? extends AppConsoleEvent>>>
            EVENT_LISTENERS = new ConcurrentHashMap<>();

    public static void register(Class<? extends AppConsoleEvent> eventClazz,
                                ConsoleEventListener<? extends AppConsoleEvent> eventListener) {
        List<ConsoleEventListener<? extends AppConsoleEvent>> listeners = EVENT_LISTENERS.get(eventClazz);
        if (listeners == null) {
            EVENT_LISTENERS.putIfAbsent(eventClazz, new ArrayList<>());
            listeners = EVENT_LISTENERS.get(eventClazz);
        }
        listeners.add(eventListener);
    }

    public static void dispatch(AppConsoleEvent event) {
        if (event == null) {
            return;
        }
        Class<? extends AppConsoleEvent> eventClazz = event.getClass();
        List<ConsoleEventListener<? extends AppConsoleEvent>> listeners = EVENT_LISTENERS.get(eventClazz);
        if (listeners == null) {
            return;
        }
        for (ConsoleEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}