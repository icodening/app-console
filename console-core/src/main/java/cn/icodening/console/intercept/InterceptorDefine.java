package cn.icodening.console.intercept;

/**
 * use ServiceLoader
 *
 * @author icodening
 * @date 2021.08.21
 */
public interface InterceptorDefine extends InterceptTypes {

    /**
     * 获取拦截点定义
     *
     * @return 拦截点
     */
    InterceptPoint[] getInterceptPoints();

}
