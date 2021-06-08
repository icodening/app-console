package cn.icodening.console.model;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class PushData {

    private String type;

    private Object data;

    private long sendTimestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    @Override
    public String toString() {
        return "PushData{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", sendTimestamp=" + sendTimestamp +
                '}';
    }
}
