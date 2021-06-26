package cn.icodening.console.extension;


import cn.icodening.console.ObjectFactory;
import cn.icodening.console.Sortable;

/**
 * @author icodening
 * @date 2021.01.01
 */
@Extensible("singleton")
public interface Scope extends Sortable {

    String SINGLETON = "singleton";

    String PROTOTYPE = "prototype";

    /**
     * 获取实例
     *
     * @param clz           实例的class
     * @param objectFactory 实例的构造工厂
     * @return 实例
     */
    Object getObject(Class<?> clz, ObjectFactory<?> objectFactory);

}
