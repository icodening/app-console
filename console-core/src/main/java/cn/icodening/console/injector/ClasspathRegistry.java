package cn.icodening.console.injector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class ClasspathRegistry {

    private final List<String> urls = new ArrayList<>();

    public void addUrl(String url) {
        urls.add(url);
    }

    public void removeUrl(String url) {
        urls.remove(url);
    }

    public List<String> getAllUrl() {
        return new ArrayList<>(urls);
    }
}
