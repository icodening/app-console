package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.event.AppConsoleEvent;
import cn.icodening.console.event.ConsoleEventListener;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.extension.ExtensionLoader;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 注册event listener
 *
 * @author icodening
 * @date 2021.06.22
 */
public class ConsoleEventListenerBootService extends BaseBootService {

    @Override
    public void start() throws AppConsoleException {
        List<ConsoleEventListener> listeners =
                ExtensionLoader.getExtensionLoader(ConsoleEventListener.class).getAllExtension();
        for (ConsoleEventListener listener : listeners) {
            Type[] genericInterfaces = listener.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (!(genericInterface instanceof ParameterizedTypeImpl)) {
                    continue;
                }
                Class<?> eventType = (Class<?>) ((ParameterizedTypeImpl) genericInterface).getActualTypeArguments()[0];
                if (AppConsoleEvent.class.isAssignableFrom(eventType)) {
                    EventDispatcher.register((Class<? extends AppConsoleEvent>) eventType, listener);
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }
}
