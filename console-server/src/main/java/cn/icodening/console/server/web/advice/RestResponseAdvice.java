package cn.icodening.console.server.web.advice;

import cn.icodening.console.server.annotation.WrapperResponse;
import cn.icodening.console.server.common.util.ConsoleResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author icodening
 * @date 2021.05.30
 */
@ControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ConsoleResponse) {
            return body;
        }
        if ((returnType.hasMethodAnnotation(WrapperResponse.class)
                || returnType.getDeclaringClass().isAnnotationPresent(WrapperResponse.class))
        ) {
            ConsoleResponse resp = new ConsoleResponse();
            resp.setData(body);
            return resp;
        }
        return body;
    }
}
