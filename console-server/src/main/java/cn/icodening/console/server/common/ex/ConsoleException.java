package cn.icodening.console.server.common.ex;

/**
 * @author icodening
 * @date 2021.05.30
 */
public class ConsoleException extends RuntimeException {

    public ConsoleException() {
        super();
    }

    public ConsoleException(String message) {
        super(message);
    }

    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
