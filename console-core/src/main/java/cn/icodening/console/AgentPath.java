package cn.icodening.console;

import cn.icodening.console.config.ConfigurationManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author icodening
 * @date 2021.05.22
 */
public enum AgentPath {
    /**
     * Instance
     */
    INSTANCE;

    private final File path;

    AgentPath() {
        path = findPath();
    }

    public File getPath() {
        return path;
    }

    private static File findPath() {
        String classResourcePath = ConfigurationManager.class.getName().replaceAll("\\.", "/") + ".class";
        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();
            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).toURI());
                    if (agentJarFile.exists()) {
                        return agentJarFile.getParentFile();
                    }
                } catch (MalformedURLException | URISyntaxException ignore) {
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString.substring(
                        prefixLength, urlString.length() - classResourcePath.length());
                return new File(classLocation);
            }
        }
        throw new AppConsoleException("config path not found!!!");
    }
}
