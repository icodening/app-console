package cn.icodening.console.server.common.util;

import java.io.Serializable;

/**
 * @author icodening
 * @date 2021.05.30
 */
public class ConsoleResponse implements Serializable {

    private boolean success = true;

    private String message = "success";

    private Object data;

    public ConsoleResponse() {
    }

    public ConsoleResponse(Object data) {
        this.data = data;
    }

    public void error(String message) {
        this.message = message;
        this.success = false;
    }

    public static ConsoleResponse ok() {
        return new ConsoleResponse();
    }

    public static ConsoleResponse ok(Object object) {
        return new ConsoleResponse(object);
    }

    public static ConsoleResponse fail(String message) {
        ConsoleResponse consoleResponse = new ConsoleResponse();
        consoleResponse.error(message);
        return consoleResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
