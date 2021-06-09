package cn.icodening.console.common.event;

import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.event.AppConsoleEvent;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class ApplicationInstanceStartedEvent extends AppConsoleEvent {

    private ApplicationInstance applicationInstance;

    public ApplicationInstanceStartedEvent(Object source) {
        super(source);
        if (source instanceof ApplicationInstance) {
            this.applicationInstance = ((ApplicationInstance) source);
        }
    }

    public ApplicationInstance getApplicationInstance() {
        return applicationInstance;
    }
}
