package cn.icodening.console.util;

import cn.icodening.console.AgentPath;
import cn.icodening.console.extension.ExtensionClassLoader;

import java.io.File;

/**
 * @author icodening
 * @date 2021.06.26
 */
public class ExtensionClassLoaderHolder {

    private static final ExtensionClassLoader EXTENSION_CLASS_LOADER;

    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        EXTENSION_CLASS_LOADER = new ExtensionClassLoader(contextClassLoader);
        EXTENSION_CLASS_LOADER.addPath(new File(AgentPath.INSTANCE.getPath() + "/extensions"));
    }

    private ExtensionClassLoaderHolder() {
    }

    public static ExtensionClassLoader get() {
        return EXTENSION_CLASS_LOADER;
    }
}
