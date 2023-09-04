package vip.sujianfeng.websocket.server;


import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;

public class NettyWebSocketServerTest {
    private static Logger logger = LoggerFactory.getLogger(NettyWebSocketServerTest.class);
    public static final int port = 8888;
    public static final String contextPath = "ws";

    public static void main(String[] args) {
        try {
            NettyWebSocketMessageEvent msgEvent = (ctx, msg) -> {
                //文本消息
                if (msg instanceof TextWebSocketFrame) {
                    logger.info("收到消息：{}", ((TextWebSocketFrame) msg).text());
                    ctx.writeAndFlush(new TextWebSocketFrame("收到"));
                    return;
                }
                //二进制消息
                if (msg instanceof BinaryWebSocketFrame) {

                    return;
                }
                //ping消息
                if (msg instanceof PongWebSocketFrame) {

                    return;
                }
            };

            new NettyWebSocketServer(port, contextPath, msgEvent).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
