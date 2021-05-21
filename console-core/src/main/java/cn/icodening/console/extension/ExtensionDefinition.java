package cn.icodening.console.extension;

import cn.icodening.console.ObjectFactory;

/**
 * @author icodening
 * @date 2021.01.05
 */
public class ExtensionDefinition<T> {
    private String name;

    private final Class<T> clazz;

    private Scope scope;

    private ObjectFactory<T> objectFactory;

    ExtensionDefinition(String name, Class<T> clazz, Scope scope, ObjectFactory<T> objectFactory) {
        this.name = name;
        this.clazz = clazz;
        this.scope = scope;
        this.objectFactory = objectFactory;
    }

    @SuppressWarnings("unchecked")
    T getTarget() {
        return (T) this.scope.getObject(clazz, objectFactory);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public ObjectFactory<T> getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory<T> objectFactory) {
        this.objectFactory = objectFactory;
    }
}