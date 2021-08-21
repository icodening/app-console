package cn.icodening.console.util;

import java.io.InputStream;

/**
 * @author icodening
 * @date 2021.07.10
 */
public class ClassUtil {

    private ClassUtil() {

    }

    public static boolean exists(String className, ClassLoader classLoader) {
        try {
            String clazzFileName = className.replaceAll("\\.", "/") + ".class";
            InputStream resourceAsStream = classLoader.getResourceAsStream(clazzFileName);
            return resourceAsStream != null;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean exists(String className) {
        try {
            return exists(className, Thread.currentThread().getContextClassLoader());
        } catch (Throwable e) {
            return false;
        }
    }
}
