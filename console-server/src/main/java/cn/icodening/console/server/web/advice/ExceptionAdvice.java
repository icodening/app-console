package cn.icodening.console.server.web.advice;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.server.util.ConsoleResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author icodening
 * @date 2021.06.13
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(AppConsoleException.class)
    public Object exHandler(AppConsoleException exception) {
        return ConsoleResponse.fail(exception.getMessage());
    }
}
