package cn.icodening.console.common.convert;

/**
 * @author icodening
 * @date 2021.06.12
 */
public interface ClassCaster {

    /**
     * 类型强转
     *
     * @param object 原类型实例
     * @return 转换后的实例
     */
    Object convert(Object object);
}
