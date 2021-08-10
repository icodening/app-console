package cn.icodening.console.util;

import java.util.*;

/**
 * @author icodening
 * @date 2021.03.09
 */
public class CaseInsensitiveKeyMap<V> implements Map<String, V> {

    private final Map<String, V> delegate;

    private final Map<String, String> caseInsensitiveKeys = new HashMap<>();

    public CaseInsensitiveKeyMap(int capacity) {
        this(new LinkedHashMap<>(capacity));
    }

    public CaseInsensitiveKeyMap() {
        this(new LinkedHashMap<>());
    }

    public CaseInsensitiveKeyMap(Map<String, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        if (key instanceof String) {
            String originKey = caseInsensitiveKeys.get(convertKey((String) key));
            try {
                return delegate.get(originKey);
            } catch (Exception exception) {
                return null;
            }
        }
        return delegate.get(key);
    }

    @Override
    public V put(String key, V value) {
        String newKey = convertKey(key);
        caseInsensitiveKeys.put(newKey, key);
        return delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String) {
            String originKey = caseInsensitiveKeys.get(convertKey((String) key));
            return delegate.remove(originKey);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        if (m == null || m.isEmpty()) {
            return;
        }
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    protected String convertKey(String key) {
        if (key == null) {
            return null;
        }
        return key.toLowerCase();
    }
}
