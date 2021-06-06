package cn.icodening.console.event;

import java.util.EventObject;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class AppConsoleEvent extends EventObject {


    private final long timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppConsoleEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
