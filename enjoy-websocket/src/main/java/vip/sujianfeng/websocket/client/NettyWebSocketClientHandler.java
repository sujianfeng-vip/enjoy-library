package vip.sujianfeng.websocket.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;

public class NettyWebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(NettyWebSocketClientHandler.class);

    private final WebSocketClientHandshaker webSocketClientHandshaker;

    private ChannelPromise handshakeFuture;

    private final NettyWebSocketContext nettyWebSocketContext;

    private Channel channel;

    private NettyWebSocketMessageEvent nettyWebsocketMessageEvent;

    public NettyWebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker, NettyWebSocketContext websocketContextNetty, NettyWebSocketMessageEvent nettyWebsocketMessageEvent) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
        this.nettyWebSocketContext = websocketContextNetty;
        this.nettyWebsocketMessageEvent = nettyWebsocketMessageEvent;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        webSocketClientHandshaker.handshake(channel);
        log.info("connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("disconnected");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            this.handleHttpRequest(msg);
            log.info("websocket connected ok !");
            return;
        }
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        if (msg instanceof CloseWebSocketFrame) {
            log.info("websocket closed!");
            channel.close();
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            this.nettyWebSocketContext.setResult(textFrame.text());
            this.nettyWebSocketContext.getCountDownLatch().countDown();
        }
        this.nettyWebsocketMessageEvent.receive(ctx, msg);
    }

    private void handleHttpRequest(Object msg) {
        webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
        handshakeFuture.setSuccess();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("exception => {}", cause.getMessage(), cause);
    }

}