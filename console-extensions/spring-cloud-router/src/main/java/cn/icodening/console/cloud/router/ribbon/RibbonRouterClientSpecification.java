package cn.icodening.console.cloud.router.ribbon;

import cn.icodening.console.cloud.router.RouterBeanPostProcessor;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;

/**
 * TODO 支持用户自定义配置文件
 *
 * @author icodening
 * @date 2021.07.17
 */
public class RibbonRouterClientSpecification extends RibbonClientSpecification {

    private static final String DEFAULT_NAME = "default.routerRibbonClientSpecification";

    public RibbonRouterClientSpecification() {
        //FIXME 用法丑陋待改进
        // EurekaSupportAutoConfiguration.class, NacosSupportAutoConfiguration.class
        this(DEFAULT_NAME, new Class[]{RouterBeanPostProcessor.class});
    }

    public RibbonRouterClientSpecification(Class<?>[] classes) {
        this(DEFAULT_NAME, classes);
    }

    public RibbonRouterClientSpecification(String name, Class<?>[] configuration) {
        super(name, configuration);
    }
}
