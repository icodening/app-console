package cn.icodening.console.common.model;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class PushData<T> {

    private String type;

    private T data;

    private long sendTimestamp;

    private String action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "PushData{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", sendTimestamp=" + sendTimestamp +
                ", action='" + action + '\'' +
                '}';
    }
}
