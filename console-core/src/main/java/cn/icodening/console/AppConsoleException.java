package cn.icodening.console;

/**
 * @author icodening
 * @date 2021.05.22
 */
public class AppConsoleException extends RuntimeException {

    public static AppConsoleException wrapperException(Throwable throwable) {
        AppConsoleException appConsoleException = new AppConsoleException(throwable.getMessage());
        appConsoleException.addSuppressed(throwable);
        return appConsoleException;
    }

    public AppConsoleException() {
    }

    public AppConsoleException(String message) {
        super(message);
    }

    public AppConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
