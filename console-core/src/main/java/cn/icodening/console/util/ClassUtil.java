package cn.icodening.console.util;

/**
 * @author icodening
 * @date 2021.07.10
 */
public class ClassUtil {

    private ClassUtil() {

    }

    public static boolean exists(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean exists(String className) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(className);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
