package cn.icodening.console.extension;

import cn.icodening.console.AgentPath;
import cn.icodening.console.ObjectFactory;
import cn.icodening.console.util.Holder;
import cn.icodening.console.util.MessageManager;
import cn.icodening.console.util.ReflectUtil;
import cn.icodening.console.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 扩展点加载器
 *
 * @author icodening
 * @date 2020.12.27
 */
public class ExtensionLoader<T> {

//    private static final Logger LOGGER = Logger.getLogger(ExtensionLoader.class);

    public static final String DEFAULT_LOAD_PATH = "META-INF/console/";

    /**
     * 扩展点加载器
     * K: 扩展点接口Class
     * V: 扩展点对应的加载器持有器
     */
    private static final ConcurrentMap<Class<?>, Holder<ExtensionLoader<?>>> EXTENSION_LOADER_HOLDER = new ConcurrentHashMap<>(32);

    private static final List<ExtensionLoadedPostProcessor> EXTENSION_LOADED_POST_PROCESSORS = new ArrayList<>(4);

    private final ConcurrentMap<String, ExtensionDefinition<T>> nameExtensionWrapperMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<Class<T>, ExtensionDefinition<T>> clazzExtensionWrapperMap = new ConcurrentHashMap<>();

    private final ClassLoader classLoader;

    /**
     * 泛型T的真实类型
     */
    private final Class<T> type;

    private boolean init = false;

    static {
        ExtensionLoader.getExtensionLoader(ExtensionLoadedPostProcessor.class);
    }

    @SuppressWarnings("unchecked")
    private void loadExtensions() {
        try {
            Map<String, Scope> nameScopeMap = new HashMap<>(4);
            Enumeration<URL> scopes = classLoader.getResources(DEFAULT_LOAD_PATH + Scope.class.getName());
            while (scopes.hasMoreElements()) {
                URL url = scopes.nextElement();
                Map<String, Class<?>> scopeMap = readFile(classLoader, url.openStream());
                scopeMap.forEach((name, clazz) -> {
                    nameScopeMap.putIfAbsent(name, ReflectUtil.newInstance((Class<Scope>) clazz));
                });
            }
            Enumeration<URL> resources = classLoader.getResources(DEFAULT_LOAD_PATH + type.getName());
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Map<String, Class<?>> nameClassMap = readFile(classLoader, url.openStream());
                nameClassMap.forEach((name, clazz) -> {
                    Extension meta = clazz.getAnnotation(Extension.class);
                    String scope = Scope.SINGLETON;
                    ObjectFactory<T> objectFactory = () -> ReflectUtil.newInstance((Class<T>) clazz);
                    if (meta != null) {
                        scope = meta.scope();
                    }
                    if (!nameScopeMap.containsKey(scope)) {
                        String scopeNotExist = MessageManager.get("scope.not.exist", scope);
//                        LOGGER.warn(scopeNotExist);
                        scope = Scope.SINGLETON;
                    }
                    ExtensionDefinition<T> extensionDefinition = new ExtensionDefinition<>(name,
                            (Class<T>) clazz,
                            nameScopeMap.get(scope),
                            objectFactory);
                    nameExtensionWrapperMap.putIfAbsent(name, extensionDefinition);
                    clazzExtensionWrapperMap.putIfAbsent((Class<T>) clazz, extensionDefinition);
                });
            }
            init = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExtensionLoader(Class<T> type) {
        this.type = type;
        ExtensionClassLoader classLoader = new ExtensionClassLoader(type.getClassLoader());
        classLoader.addPath(new File(AgentPath.INSTANCE.getPath() + "/extensions"));
        this.classLoader = classLoader;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(Extensible.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clz) {
        Objects.requireNonNull(clz, MessageManager.get("extension.type.is.require.not.null"));
        if (!clz.isInterface()) {
            throw new IllegalArgumentException(MessageManager.get("extension.type.is.not.interface", clz));
        }
        if (!withExtensionAnnotation(clz)) {
            throw new IllegalArgumentException(MessageManager.get("extensible.not.exist", "@" + Extensible.class.getName()));
        }
        Holder<ExtensionLoader<?>> extensionLoaderHolder = EXTENSION_LOADER_HOLDER.get(clz);
        if (extensionLoaderHolder == null) {
            EXTENSION_LOADER_HOLDER.putIfAbsent(clz, new Holder<>());
        }
        extensionLoaderHolder = EXTENSION_LOADER_HOLDER.get(clz);
        ExtensionLoader<?> extensionLoader = extensionLoaderHolder.get();
        if (extensionLoader == null || !extensionLoader.init) {
            synchronized (extensionLoaderHolder) {
                extensionLoader = extensionLoaderHolder.get();
                if (extensionLoader == null || !extensionLoader.init) {
                    ExtensionLoader<T> loader = new ExtensionLoader<>(clz);
                    loader.loadExtensions();
                    invokeExtensionLoadedPostProcessor(loader);
                    extensionLoaderHolder.set(loader);
                    extensionLoader = extensionLoaderHolder.get();
                }
            }
        }
        return (ExtensionLoader<T>) extensionLoader;
    }

    @SuppressWarnings("unchecked")
    private static void invokeExtensionLoadedPostProcessor(ExtensionLoader<?> extensionLoader) {
        if (ExtensionLoadedPostProcessor.class.isAssignableFrom(extensionLoader.type)) {
            synchronized (EXTENSION_LOADED_POST_PROCESSORS) {
                List<?> allExtension = extensionLoader.getAllExtension();
                EXTENSION_LOADED_POST_PROCESSORS.addAll((Collection<? extends ExtensionLoadedPostProcessor>) allExtension);
            }
        }
        for (ExtensionLoadedPostProcessor extensionLoadedPostProcessor : EXTENSION_LOADED_POST_PROCESSORS) {
            Collection<? extends ExtensionDefinition<?>> values = extensionLoader.nameExtensionWrapperMap.values();
            List<ExtensionDefinition<?>> extensionDefinitions = new ArrayList<>(values);
            extensionLoadedPostProcessor.postLoaded(extensionDefinitions, extensionLoader);
        }
    }

    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        ExtensionDefinition<T> extensionDefinition = nameExtensionWrapperMap.get(name);
        Objects.requireNonNull(extensionDefinition, MessageManager.get("extension.not.exist", name));
        return extensionDefinition.getTarget();
    }

    public T getExtension(Class<? extends T> clz) {
        ExtensionDefinition<T> wrapper = clazzExtensionWrapperMap.get(clz);
        return getExtension(wrapper.getName());
    }

    public List<T> getAllExtension() {
        Set<String> names = nameExtensionWrapperMap.keySet();
        List<T> extensions = new ArrayList<>();
        for (String name : names) {
            T extension = getExtension(name);
            extensions.add(extension);
        }
        return extensions;
    }

    /**
     * 无参数则获取默认实现
     */
    @SuppressWarnings("unchecked")
    public T getExtension() {
        Extensible annotation = this.type.getAnnotation(Extensible.class);
        T result = null;
        if (!StringUtil.isBlank(annotation.value())) {
            result = getExtension(annotation.value());
        }
        if (null == result) {
//            LOGGER.warn(MessageManager.get("default.extension.not.exist", type));
        }
        return result;
    }

    private Map<String, Class<?>> readFile(ClassLoader classLoader, InputStream inputStream) {
        Map<String, Class<?>> nameClassMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String row;
            while ((row = bufferedReader.readLine()) != null) {
                if (row.startsWith("#")) {
                    continue;
                }
                int equalsIndexOf = row.indexOf("=");
                String key = row;
                if (equalsIndexOf > 0) {
                    key = row.substring(0, equalsIndexOf);
                }
                String value = row.substring(equalsIndexOf + 1);
                Class<?> clz = classLoader.loadClass(value);
                nameClassMap.put(key, clz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameClassMap;
    }
}
