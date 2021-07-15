package cn.icodening.console.log;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.nio.ByteBuffer;

/**
 * WebSocket 分片传输工具类, 主要用于分片传输超大数据包
 *
 * @author icodening
 * @date 2021.07.15
 */
class WebSocketUtil {

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private WebSocketUtil() {
    }

    static void fragmentTransmission(Session session, byte[] srcBytes) {
        fragmentTransmission(session, srcBytes, DEFAULT_BUFFER_SIZE);
    }

    static void fragmentTransmission(Session session, byte[] srcBytes, int fragmentSize) {
        try {
            final RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
            if (fragmentSize >= srcBytes.length) {
                basicRemote.sendBinary(ByteBuffer.wrap(srcBytes));
                return;
            }
            ByteBuffer buffer = ByteBuffer.allocate(fragmentSize);
            int srcLength = srcBytes.length;
            int count = srcLength / fragmentSize;
            int mod = srcLength % fragmentSize;
            byte[] dest = new byte[fragmentSize];
            int pos = 0;
            for (int i = 0; i < count; i++, pos += fragmentSize) {
                System.arraycopy(srcBytes, pos, dest, 0, fragmentSize);
                buffer.put(dest);
                buffer.flip();
                basicRemote.sendBinary(buffer);
                buffer.clear();
            }
            System.arraycopy(srcBytes, pos, dest, 0, mod);
            buffer.put(dest, 0, mod);
            buffer.flip();
            basicRemote.sendBinary(buffer);
        } catch (Exception ignore) {

        }
    }
}
