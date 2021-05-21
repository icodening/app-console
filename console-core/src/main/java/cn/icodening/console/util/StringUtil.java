package cn.icodening.console.util;

/**
 * @author icodening
 * @date 2020.12.26
 */
public class StringUtil {

    private StringUtil() {
    }

    public static boolean isBlank(String string) {
        return string != null && "".equals(string.trim());
    }
}
