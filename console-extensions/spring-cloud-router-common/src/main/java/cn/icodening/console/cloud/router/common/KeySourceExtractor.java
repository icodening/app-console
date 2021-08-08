package cn.icodening.console.cloud.router.common;

/**
 * 键值提取器，如从请求header、query...提取相关值
 *
 * @author icodening
 * @date 2021.07.19
 */
public interface KeySourceExtractor<T> {

    /**
     * 判断指定的key是否存在于该请求中
     *
     * @param target 请求
     * @param name   Key的名字
     * @return true存在， false不存在
     */
    boolean contains(T target, String name);

    /**
     * 提取值
     *
     * @param target 请求，例如{@link org.springframework.http.HttpRequest}、{@link feign.RequestTemplate}
     * @param name   Key的名字
     * @return key对应的value值
     */
    String getValue(T target, String name);
}
