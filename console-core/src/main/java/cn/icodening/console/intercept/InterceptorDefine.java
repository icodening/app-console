package cn.icodening.console.intercept;

/**
 * use ServiceLoader
 *
 * @author icodening
 * @date 2021.08.21
 */
public interface InterceptorDefine {

    /**
     * 需要拦截的class
     *
     * @return classname
     */
    String type();

    /**
     * 获取拦截点定义
     *
     * @return 拦截点
     */
    InterceptPoint[] getInterceptPoints();

}
