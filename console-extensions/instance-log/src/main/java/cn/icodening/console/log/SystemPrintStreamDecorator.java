package cn.icodening.console.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class SystemPrintStreamDecorator extends PrintStream {

    private static final int DEFAULT_MAX_BUFFER_SIZE = 8 * 1024;

    private static final int DEFAULT_INIT_BUFFER_SIZE = 1024;

    private final OutputStream delegate;

    private final Set<BytesConsumer> flushCallbacks = Collections.synchronizedSet(new HashSet<>());

    private volatile ByteArrayOutputStream bos = new ByteArrayOutputStream(DEFAULT_INIT_BUFFER_SIZE);

    public SystemPrintStreamDecorator(OutputStream systemOut, OutputStream delegate) {
        super(systemOut);
        this.delegate = delegate;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        try {
            delegate.write(buf, off, len);
            bos.write(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerFlushCallback(BytesConsumer callback) {
        flushCallbacks.add(callback);
    }

    public void deregisterFlushCallback(BytesConsumer callback) {
        flushCallbacks.remove(callback);
    }

    @Override
    public void flush() {
        super.flush();
        try {
            delegate.flush();
            for (BytesConsumer flushCallback : flushCallbacks) {
                flushCallback.accept(bos.toByteArray());
            }
            if (DEFAULT_MAX_BUFFER_SIZE < bos.size()) {
                bos = null;
                bos = new ByteArrayOutputStream(DEFAULT_INIT_BUFFER_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
