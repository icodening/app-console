package cn.icodening.console.log;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author icodening
 * @date 2021.07.14
 */
class WebSocketLogPushBytesConsumer extends BytesConsumer {

    private final Session session;

    WebSocketLogPushBytesConsumer(Session session) {
        this.session = session;
    }

    @Override
    public void accept(byte[] bytes) {
        try {
            if (!session.isOpen()) {
                return;
            }
            ByteBuffer wrap = ByteBuffer.wrap(bytes);
            session.getBasicRemote().sendBinary(wrap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
