package cn.icodening.console.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 包装响应.
 * 用于controller层return任何值时都能统一包装为 {@link cn.icodening.console.server.util.ConsoleResponse}
 *
 * @author icodening
 * @date 2021.05.31
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapperResponse {
}
