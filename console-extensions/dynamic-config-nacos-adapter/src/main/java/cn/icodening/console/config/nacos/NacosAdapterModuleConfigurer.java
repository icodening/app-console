package cn.icodening.console.config.nacos;

import cn.icodening.console.config.DynamicConfigModuleRegister;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.08.03
 */
public class NacosAdapterModuleConfigurer implements DynamicConfigModuleRegister {

    private static final String NACOS_VALUE_PROCESSOR = "com.alibaba.nacos.spring.context.annotation.config.NacosValueAnnotationBeanPostProcessor";

    private static final String NACOS_CONFIG_BOOTSTRAP_CLASS = "com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration";

    @Override
    public boolean shouldRegister() {
        boolean existsNacosBoot = ClassUtil.exists(NACOS_VALUE_PROCESSOR);
        boolean existsNacosCloud = ClassUtil.exists(NACOS_CONFIG_BOOTSTRAP_CLASS);
        return existsNacosBoot || existsNacosCloud;
    }
}
