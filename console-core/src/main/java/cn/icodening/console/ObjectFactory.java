package cn.icodening.console;

/**
 * @author icodening
 * @date 2021.01.03
 */
public interface ObjectFactory<E> {

    /**
     * 获得一个Extension实例
     * @return Extension实例
     */
    E getObject();
}
