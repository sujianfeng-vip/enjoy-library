package vip.sujianfeng.websocket.intf;

import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端消息
 * author SuJianFeng
 * createTime  2023/6/14
 * @Description
 **/
public interface NettyWebSocketMessageEvent {
    void receive(ChannelHandlerContext ctx, Object msg);
}
