package cn.icodening.console.log;

import javax.websocket.Session;

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
    public void accept(byte[] lineBytes) {
        WebSocketUtil.fragmentTransmission(session, lineBytes);
    }
}
