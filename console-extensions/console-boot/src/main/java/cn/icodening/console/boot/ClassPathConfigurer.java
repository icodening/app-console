package cn.icodening.console.boot;

import cn.icodening.console.extension.Extensible;

/**
 * @author icodening
 * @date 2021.06.06
 */
@Extensible
public interface ClassPathConfigurer {

    void configurerClassPath(ClassPathRegistry classPathRegistry);
}

