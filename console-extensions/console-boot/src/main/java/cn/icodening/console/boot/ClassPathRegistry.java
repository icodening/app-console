package cn.icodening.console.boot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class ClassPathRegistry {

    private final List<String> urls = new ArrayList<>();

    public void addURL(String url) {
        urls.add(url);
    }

    public void removeURL(String url) {
        urls.remove(url);
    }

    public List<String> getAllURL() {
        return new ArrayList<>(urls);
    }
}
