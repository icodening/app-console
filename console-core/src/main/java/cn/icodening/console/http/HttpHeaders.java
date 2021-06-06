package cn.icodening.console.http;


import cn.icodening.console.util.CaseInsensitiveKeyMap;
import cn.icodening.console.util.MultiValueMap;
import cn.icodening.console.util.SimpleMultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.03.09
 */
public class HttpHeaders implements MultiValueMap<String, String> {

    private final MultiValueMap<String, String> targetMap;

    public HttpHeaders() {
        this(new SimpleMultiValueMap<>(new CaseInsensitiveKeyMap<>()));
    }

    public HttpHeaders(MultiValueMap<String, String> targetMap) {
        this.targetMap = targetMap;
    }

    @Override
    public String getFirst(String key) {
        return targetMap.getFirst(key);
    }

    @Override
    public void add(String key, String value) {
        targetMap.add(key, value);
    }

    @Override
    public void addAll(String key, List<? extends String> values) {
        targetMap.addAll(key, values);
    }

    @Override
    public void addAll(MultiValueMap<String, String> values) {
        targetMap.addAll(values);
    }

    @Override
    public void set(String key, String value) {
        targetMap.set(key, value);
    }

    @Override
    public void setAll(Map<String, String> values) {
        targetMap.setAll(values);
    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return targetMap.toSingleValueMap();
    }

    @Override
    public int size() {
        return targetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return targetMap.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return targetMap.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return targetMap.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        targetMap.putAll(m);
    }

    @Override
    public void clear() {
        targetMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return targetMap.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return targetMap.values();
    }

    @Override
    public Set<Map.Entry<String, List<String>>> entrySet() {
        return targetMap.entrySet();
    }

    @Override
    public String toString() {
        return targetMap.toString();
    }
}
