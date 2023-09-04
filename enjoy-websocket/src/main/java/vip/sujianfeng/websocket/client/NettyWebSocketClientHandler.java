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

    /**
     * 连接处理器
     */
    private final WebSocketClientHandshaker webSocketClientHandshaker;

    /**
     * netty提供的数据过程中的数据保证
     */
    private ChannelPromise handshakeFuture;

    /**
     * 任务上下文
     */
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

    /**
     * ChannelHandler添加到实际上下文中准备处理事件,调用此方法
     *
     * @param ctx ChannelHandlerContext
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    /**
     * 当客户端主动链接服务端的链接后,调用此方法
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        webSocketClientHandshaker.handshake(channel);
        log.info("建立连接");
    }

    /**
     * 链接断开后,调用此方法
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("连接断开");
    }

    /**
     * 接收消息,调用此方法
     *
     * @param ctx ChannelHandlerContext
     * @param msg Object
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            this.handleHttpRequest(msg);
            log.info("websocket已经建立连接");
            return;
        }
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        if (msg instanceof CloseWebSocketFrame) {
            log.info("连接收到关闭帧");
            channel.close();
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            //log.info("收到消息 => {}", ((TextWebSocketFrame) msg).text());
            this.nettyWebSocketContext.setResult(textFrame.text());
            this.nettyWebSocketContext.getCountDownLatch().countDown();
        }
        this.nettyWebsocketMessageEvent.receive(ctx, msg);
    }

    /**
     * 处理http连接请求.<br>
     *
     * @param msg:
     * @return:
     */
    private void handleHttpRequest(Object msg) {
        webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
        handshakeFuture.setSuccess();
    }

    /**
     * 运行过程中未捕获的异常,调用此方法
     *
     * @param ctx ChannelHandlerContext
     * @param cause Throwable
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("监控触发异常=>{}", cause.getMessage(), cause);
    }

}