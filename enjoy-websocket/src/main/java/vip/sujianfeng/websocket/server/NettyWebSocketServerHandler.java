package vip.sujianfeng.websocket.server;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //Client Group
    public static ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //Container for storing IP and channels
    private static ConcurrentMap<String, Channel> IP_CHANNEL_MAP = new ConcurrentHashMap<>();

    private NettyWebSocketMessageEvent nettyWebsocketMessageEvent;

    public NettyWebSocketServerHandler(NettyWebSocketMessageEvent nettyWebsocketMessageEvent) {
        this.nettyWebsocketMessageEvent = nettyWebsocketMessageEvent;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CHANNEL_GROUP.add(ctx.channel());
        String ip = getAddr(ctx);
        // Save the relationship between IP and channel
        if (!IP_CHANNEL_MAP.containsKey(ip)){
            IP_CHANNEL_MAP.put(ip, ctx.channel());
        }
        logger.info("Client [{}] successfully connected!", ip);
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String clintInfo = String.format("Message from client[%s]", getAddr(ctx));
        logger.info("{} => {},{}", clintInfo, msg.getClass().getName(), JSON.toJSONString(msg));
        if (msg instanceof FullHttpRequest){
            //Accessing via HTTP request, but using websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            return;
        }
        //turn off message
        if (msg instanceof CloseWebSocketFrame) {
            logger.info("{} => close", clintInfo);
            Channel channel = ctx.channel();
            channel.close();
            return;
        }
        //Accept message processing
        nettyWebsocketMessageEvent.receive(ctx, msg);
        /*
        //Text Message
        if (msg instanceof TextWebSocketFrame) {
            logger.info("Received customer message:{}", ((TextWebSocketFrame) msg).text());
            ctx.writeAndFlush(new TextWebSocketFrame("receive"));
            return;
        }
        //Binary Message
        if (msg instanceof BinaryWebSocketFrame) {

            return;
        }
        //Ping message
        if (msg instanceof PongWebSocketFrame) {

            return;
        }
        */
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client[{}] => channelUnregistered", getAddr(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client [{}]=>Disconnect", getAddr(ctx));
        CHANNEL_GROUP.remove(ctx.channel());
        IP_CHANNEL_MAP.remove(getAddr(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection exception:" + cause.getMessage());
        ctx.close();
        IP_CHANNEL_MAP.remove(getAddr(ctx));
    }

    public void sendMessage(String address){
        Channel channel= IP_CHANNEL_MAP.get(address);
        String message = "Hello, this is the designated message for sending";
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public void sendMessageAll(String message){
        CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(message));
    }

    public String getAddr(ChannelHandlerContext ctx){
        //Obtain the IP address bound to the current channel
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String address = ipSocket.getAddress().getHostAddress();
        return String.format("%s:%s", address, ipSocket.getPort());
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //Require Upgrade to be websocket, filter out get/Post
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //If not in websocket mode, create BAD_ REQUEST's req, returned to the client
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8081/websocket", null, false);
        WebSocketServerHandshaker handshakes = wsFactory.newHandshaker(req);
        if (handshakes == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }
        handshakes.handshake(ctx.channel(), req);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // Return the response to the client
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // If it is not Keep Alive, close the connection
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public NettyWebSocketMessageEvent getMessageEvent() {
        return nettyWebsocketMessageEvent;
    }
}
