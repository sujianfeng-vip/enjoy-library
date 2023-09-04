package vip.sujianfeng.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;

import java.net.InetSocketAddress;

public class NettyWebSocketServer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int port;
    private String contextPath;
    private NettyWebSocketMessageEvent nettyWebsocketMessageEvent;

    public NettyWebSocketServer(int port, String contextPath, NettyWebSocketMessageEvent nettyWebsocketMessageEvent) {
        this.port = port;
        this.contextPath = contextPath;
        this.nettyWebsocketMessageEvent = nettyWebsocketMessageEvent;
    }

    public void run() throws InterruptedException {
        // netty基本操作，两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //netty的启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    //记录日志的handler，netty自带的
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_BACKLOG,1024 * 1024 * 10)
                    //设置handler
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //websocket协议本身是基于Http协议的，所以需要Http解码器
                            pipeline.addLast("http-codec",new HttpServerCodec());
                            //以块的方式来写的处理器
                            pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                            //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                            pipeline.addLast("aggregator",new HttpObjectAggregator(1024 * 1024 * 1024));
                            //这个是websocket的handler，是netty提供的，也可以自定义，建议就用默认的
                            pipeline.addLast(new WebSocketServerProtocolHandler("/" + contextPath,null,true,65535));
                            //自定义的handler，处理服务端传来的消息
                            pipeline.addLast(new NettyWebSocketServerHandler(nettyWebsocketMessageEvent));
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            channelFuture.channel().closeFuture().sync();
            logger.info("Netty服务[{}/{}]已启动!", port, contextPath);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            logger.info("Netty服务[{}/{}]已关闭!", port, contextPath);
        }
    }
}
