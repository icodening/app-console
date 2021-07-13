package cn.icodening.console.log;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author icodening
 * @date 2021.07.12
 */
abstract class BytesConsumer implements Consumer<byte[]> {

    private final String id;

    BytesConsumer() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BytesConsumer that = (BytesConsumer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
