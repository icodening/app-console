package cn.icodening.console.common.event;

import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.AppConsoleEvent;

/**
 * @author icodening
 * @date 2021.06.10
 */
public class ServerMessageReceivedEvent extends AppConsoleEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ServerMessageReceivedEvent(ServerMessage source) {
        super(source);
    }

    @Override
    public ServerMessage getSource() {
        return (ServerMessage) super.getSource();
    }
}
