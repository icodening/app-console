package cn.icodening.console.listener;

/**
 * @author icodening
 * @date 2021.05.23
 */
public class Message<T> {

    private T message;

    private String type;

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
