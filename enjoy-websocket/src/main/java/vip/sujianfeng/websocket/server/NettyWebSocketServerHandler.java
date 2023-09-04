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
    //客户端组
    public static ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //存储ip和channel的容器
    private static ConcurrentMap<String, Channel> IP_CHANNEL_MAP = new ConcurrentHashMap<>();

    private NettyWebSocketMessageEvent nettyWebsocketMessageEvent;

    public NettyWebSocketServerHandler(NettyWebSocketMessageEvent nettyWebsocketMessageEvent) {
        this.nettyWebsocketMessageEvent = nettyWebsocketMessageEvent;
    }

    /**
     * 增加连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CHANNEL_GROUP.add(ctx.channel());
        String ip = getAddr(ctx);
        //将IP和channel的关系保存
        if (!IP_CHANNEL_MAP.containsKey(ip)){
            IP_CHANNEL_MAP.put(ip, ctx.channel());
        }
        logger.info("客户端[{}]连接成功!", ip);
    }

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String clintInfo = String.format("来自客户端消息[%s]", getAddr(ctx));
        logger.info("{} => {},{}", clintInfo, msg.getClass().getName(), JSON.toJSONString(msg));
        if (msg instanceof FullHttpRequest){
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            return;
        }
        //关闭消息
        if (msg instanceof CloseWebSocketFrame) {
            logger.info("{} => 关闭", clintInfo);
            Channel channel = ctx.channel();
            channel.close();
            return;
        }
        //接受消息处理
        nettyWebsocketMessageEvent.receive(ctx, msg);
        /*
        //文本消息
        if (msg instanceof TextWebSocketFrame) {
            logger.info("收到客户消息：{}", ((TextWebSocketFrame) msg).text());
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
        */
    }

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端[{}] => channelUnregistered", getAddr(ctx));
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端[{}] => 断开连接", getAddr(ctx));
        CHANNEL_GROUP.remove(ctx.channel());
        IP_CHANNEL_MAP.remove(getAddr(ctx));
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接异常："+cause.getMessage());
        ctx.close();
        IP_CHANNEL_MAP.remove(getAddr(ctx));
    }


    /**
     * 给指定客户端发消息
     */
    public void sendMessage(String address){
        Channel channel= IP_CHANNEL_MAP.get(address);
        String message="你好，这是指定消息发送";
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * 群发消息
     */
    public void sendMessageAll(String message){
        CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(message));
    }

    public String getAddr(ChannelHandlerContext ctx){
        //获取当前channel绑定的IP地址
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String address = ipSocket.getAddress().getHostAddress();
        return String.format("%s:%s", address, ipSocket.getPort());
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
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

    /**
     * 拒绝不合法的请求，并返回错误信息
     * */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public NettyWebSocketMessageEvent getMessageEvent() {
        return nettyWebsocketMessageEvent;
    }
}
