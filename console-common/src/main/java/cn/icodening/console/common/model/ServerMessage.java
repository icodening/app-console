package cn.icodening.console.common.model;

/**
 * @author icodening
 * @date 2021.06.10
 */
public class ServerMessage {

    private String type;

    private long sendTimestamp;

    private String action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
