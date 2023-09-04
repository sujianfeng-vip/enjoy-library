package vip.sujianfeng.websocket.client;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;
import vip.sujianfeng.websocket.server.NettyWebSocketServerTest;

public class NettyWebSocketClientTest {

    private static Logger logger = LoggerFactory.getLogger(NettyWebSocketClientTest.class);


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
            NettyWebSocketClient websocketClientNetty = new NettyWebSocketClient(
                    String.format("ws://localhost:%s/%s", NettyWebSocketServerTest.port, NettyWebSocketServerTest.contextPath),
                    15,
                    msgEvent);
            // 连接
            websocketClientNetty.connect();
            // 发送消息
            websocketClientNetty.send("你好!");
            //收到消息
            String s = websocketClientNetty.receiveResult();
            logger.info("收到消息 => {}", s);
            //等待下，服务会返回消息
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }


    }

}
