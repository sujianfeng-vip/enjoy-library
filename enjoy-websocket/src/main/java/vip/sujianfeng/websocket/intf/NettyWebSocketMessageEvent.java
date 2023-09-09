package vip.sujianfeng.websocket.intf;

import io.netty.channel.ChannelHandlerContext;

public interface NettyWebSocketMessageEvent {
    void receive(ChannelHandlerContext ctx, Object msg);
}
