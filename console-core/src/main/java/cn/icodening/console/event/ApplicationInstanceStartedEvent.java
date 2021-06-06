package cn.icodening.console.event;

import cn.icodening.console.model.ApplicationInstance;

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
