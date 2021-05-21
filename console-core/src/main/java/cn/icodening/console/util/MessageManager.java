package cn.icodening.console.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.01.25
 */
public class MessageManager {

    private static final String MESSAGE_FILE_PREFIX = "message";

    private static final String MESSAGE_FILE_SUFFIX = ".properties";

//    private static final Logger LOGGER = Logger.getLogger(cn.icodening.rpc.core.util.MessageManager.class);

    private static final String LOCALE_LANGUAGE = Locale.getDefault().toString();

    private static final String DEFAULT_LANGUAGE = "";

    private static final Map<String, MessageManager> I18N_UTILS = new ConcurrentHashMap<>();

    private final Map<String, String> messageMap = new ConcurrentHashMap<>();

    private final String language;

    static {
        getInstance(DEFAULT_LANGUAGE);
    }

    private MessageManager(String language) {
        this.language = language;
    }

    private static String getResourcesName(String language) {
        if (StringUtil.isBlank(language)) {
            return MESSAGE_FILE_PREFIX + MESSAGE_FILE_SUFFIX;
        }
        return MESSAGE_FILE_PREFIX + "_" + language + MESSAGE_FILE_SUFFIX;
    }

    public static MessageManager getInstance() {
        MessageManager instance = getInstance(LOCALE_LANGUAGE);
        if (instance == null) {
            instance = getInstance(DEFAULT_LANGUAGE);
        }
        return instance;
    }

    public static String get(String code, Object... args) {
        return getInstance().getMessage(code, args);
    }

    public static MessageManager getInstance(String language) {
        language = language.intern();
        MessageManager messageManager = I18N_UTILS.get(language);
        if (messageManager == null) {
            synchronized (language) {
                messageManager = I18N_UTILS.get(language);
                if (messageManager == null) {
                    I18N_UTILS.putIfAbsent(language, new MessageManager(language));
                    messageManager = I18N_UTILS.get(language);
                    try {
                        Enumeration<URL> resources =
                                Thread.currentThread().getContextClassLoader()
                                        .getResources(getResourcesName(language));
                        while (resources.hasMoreElements()) {
                            URL url = resources.nextElement();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                            String ret;
                            while ((ret = bufferedReader.readLine()) != null) {
                                int index = ret.indexOf("=");
                                String code = ret.substring(0, index);
                                String message = ret.substring(index + 1);
                                messageManager.messageMap.putIfAbsent(code, message.intern());
                            }
                        }
                    } catch (IOException e) {
//                        LOGGER.error(e);
                    } finally {
                        if (!DEFAULT_LANGUAGE.equals(messageManager.language)
                                && messageManager.messageMap.isEmpty()) {
                            messageManager = null;
                            I18N_UTILS.remove(language);
                            I18N_UTILS.putIfAbsent(language, I18N_UTILS.get(DEFAULT_LANGUAGE));
                        }
                    }
                }
            }
        }
        return messageManager;
    }

    public String getMessage(String code, Object... args) {
        String value = messageMap.get(code);
        if (value == null) {
            return code;
        }
        return String.format(value, args);
    }
}
