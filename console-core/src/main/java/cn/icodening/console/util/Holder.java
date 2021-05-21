package cn.icodening.console.util;

/**
 * 对象持有工具
 * {@link java.util.Optional} 不太方便
 *
 * @author icodening
 * @date 2020.12.27
 */
public class Holder<V> {

    private V value;

    public Holder() {

    }

    public Holder(V value) {
        this.value = value;
    }

    public void set(V value) {
        this.value = value;
    }

    public V get() {
        return value;
    }
}
