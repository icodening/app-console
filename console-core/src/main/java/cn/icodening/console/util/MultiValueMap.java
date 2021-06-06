package cn.icodening.console.util;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author icodening
 * @date 2021.03.09
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    V getFirst(K key);

    default V getFirst(K key, V defaultValue) {
        V first = getFirst(key);
        if (first == null) {
            return defaultValue;
        }
        return first;
    }

    void add(K key, V value);

    void addAll(K key, List<? extends V> values);

    void addAll(MultiValueMap<K, V> values);

    default void addIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    void set(K key, V value);

    void setAll(Map<K, V> values);

    Map<K, V> toSingleValueMap();

    default void firstForeach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, List<V>> entry : entrySet()) {
            K k;
            V v = null;
            try {
                k = entry.getKey();
                if (entry.getValue() != null) {
                    v = entry.getValue().get(0);
                }
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

}
