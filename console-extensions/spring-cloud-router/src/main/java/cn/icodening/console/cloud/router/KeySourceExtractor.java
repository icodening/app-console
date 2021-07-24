package cn.icodening.console.cloud.router;

/**
 * 键值提取器，如从请求header、query...提取相关值
 *
 * @author icodening
 * @date 2021.07.19
 */
public interface KeySourceExtractor<T> {

    /**
     * 提取值
     *
     * @param target 请求类型，例如{@link org.springframework.http.HttpRequest}、{@link feign.RequestTemplate}
     * @param name   Key的名字
     * @return key对应的value值
     */
    String getValue(T target, String name);
}
