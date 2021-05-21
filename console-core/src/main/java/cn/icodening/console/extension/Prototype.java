package cn.icodening.console.extension;

import cn.icodening.console.ObjectFactory;

/**
 * @author icodening
 * @date 2021.01.03
 */
public class Prototype implements Scope {

    @Override
    public Object getObject(Class<?> clz, ObjectFactory<?> objectFactory) {
        return objectFactory.getObject();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().toLowerCase();
    }
}
