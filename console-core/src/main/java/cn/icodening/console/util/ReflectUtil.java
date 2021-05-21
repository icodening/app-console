package cn.icodening.console.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 反射工具类
 *
 * @author icodening
 * @date 2020.12.27
 */
public class ReflectUtil {
    private ReflectUtil() {
    }

    private static final Map<Class<?>, Method[]> DECLARED_METHODS_CACHE = new ConcurrentHashMap<>(256);
    private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

    private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
        return (paramTypes.length == method.getParameterCount() &&
                Arrays.equals(paramTypes, method.getParameterTypes()));
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() :
                    getDeclaredMethods(searchType, false));
            for (Method method : methods) {
                if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static List<Method> findAnnotationMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Objects.requireNonNull(clazz, MessageManager.get("object.required.not.null"));
        Objects.requireNonNull(annotationClass, MessageManager.get("object.required.not.null"));
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> methods = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            Annotation declaredAnnotation = declaredMethod.getDeclaredAnnotation(annotationClass);
            if (declaredAnnotation == null) {
                continue;
            }
            methods.add(declaredMethod);
        }
        return methods;
    }

    /**
     * 获取指定类对应的接口中的方法
     *
     * @param clazz 目标类
     * @return 方法list
     */
    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> interfaceClazz : clazz.getInterfaces()) {
            for (Method interfaceMethod : interfaceClazz.getMethods()) {
                if (!Modifier.isAbstract(interfaceMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(interfaceMethod);
                }
            }
        }
        return result;
    }

    public static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
        Method[] result = DECLARED_METHODS_CACHE.get(clazz);
        if (result == null) {
            try {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
                if (defaultMethods != null) {
                    result = new Method[declaredMethods.length + defaultMethods.size()];
                    System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                    int index = declaredMethods.length;
                    for (Method defaultMethod : defaultMethods) {
                        result[index] = defaultMethod;
                        index++;
                    }
                } else {
                    result = declaredMethods;
                }
                DECLARED_METHODS_CACHE.put(clazz, (result.length == 0 ? EMPTY_METHOD_ARRAY : result));
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                        "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }
        return (result.length == 0 || !defensive) ? result : result.clone();
    }

    private static boolean isOverridable(Method method, Class<?> targetClass) {
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return true;
        }
        return (targetClass == null ||
                getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
    }

    public static String getPackageName(Class<?> clazz) {
        return getPackageName(clazz.getName());
    }

    public static String getPackageName(String fqClassName) {
        int lastDotIndex = fqClassName.lastIndexOf(".");
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    /**
     * 获取当前方法对应的最佳方法
     *
     * @param method      方法
     * @param targetClass 目标真实类的class
     * @return 最佳匹配的方法
     */
    public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
        if (targetClass != null && targetClass != method.getDeclaringClass() && isOverridable(method, targetClass)) {
            try {
                if (Modifier.isPublic(method.getModifiers())) {
                    try {
                        return targetClass.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException ex) {
                        return method;
                    }
                } else {
                    Method specificMethod =
                            findMethod(targetClass, method.getName(), method.getParameterTypes());
                    return (specificMethod != null ? specificMethod : method);
                }
            } catch (SecurityException ex) {
                // Security settings are disallowing reflective access; fall back to 'method' below.
            }
        }
        return method;
    }


    public static <T> T newInstance(Class<T> clz) {
        try {
            Constructor<T> constructor = clz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {

        }
        return null;
    }

    public static <T> T newInstance(String clzName) {
        try {
            Class<T> clz = (Class<T>) Class.forName(clzName);
            return newInstance(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Objects.requireNonNull(clazz);
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, Field field) {
        Object value = null;
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            value = field.get(object);
        } catch (IllegalAccessException ignored) {

        }
        return (T) value;
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void makeAccessible(Method method) {
        if (method.isAccessible()) {
            return;
        }
        if (!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessible(Field field) {
        if (field.isAccessible()) {
            return;
        }
        if (!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static Class<?>[] getAllInterfaces(Class<?> clazz) {
        Class<?> temp = clazz;
        Set<Class<?>> interfaces = new HashSet<>();
        while (!Object.class.equals(temp)) {
            interfaces.addAll(Arrays.asList(temp.getInterfaces()));
            temp = temp.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[0]);
    }

    /**
     * 获取指定Class上的泛型
     */
    public static Class<?> findGenericClass(Class<?> clz) {
        return findGenericClass(clz, 0);
    }

    public static Class<?> findGenericClass(Class<?> clz, int index) {
        if (clz == null) {
            return null;
        }
        Type genericSuperclass = clz.getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (Object.class.equals(genericSuperclass)) {
            Type[] interfaces = clz.getGenericInterfaces();
            try {
                parameterizedType = (ParameterizedType) interfaces[index];
            } catch (ClassCastException e) {
//                LOGGER.warn(e.getMessage());
                return null;
            }

            return (Class<?>) parameterizedType.getActualTypeArguments()[index];
        }
        parameterizedType = (ParameterizedType) genericSuperclass;
        if (parameterizedType == null) {
            return null;
        }
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types.length == 0) {
            return null;
        }
        return (Class<?>) types[index];
    }

    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw (Exception) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        return invokeMethod(method, target, null, args);
    }

    public static Object invokeMethod(Method method, Object target, Consumer<Throwable> exHandler, Object... args) {
        try {
            makeAccessible(method);
            return method.invoke(target, args);
        } catch (Throwable e) {
            if (exHandler != null) {
                exHandler.accept(e);
            }
        }
        return null;
    }

}
