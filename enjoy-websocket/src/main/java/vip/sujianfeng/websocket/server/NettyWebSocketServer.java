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
        // Netty basic operation, two thread groups
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //Netty's startup class
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    //The handler for logging, which comes with Netty
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_BACKLOG,1024 * 1024 * 10)
                    //Set handler
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //The websocket protocol itself is based on the HTTP protocol, so an HTTP decoder is required
                            pipeline.addLast("http-codec",new HttpServerCodec());
                            //Processors written in blocks
                            pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                            //Netty is based on segmented requests, and the function of HttpObject Aggregator is to segment and reassemble requests. The parameter is the maximum length of aggregated bytes
                            pipeline.addLast("aggregator",new HttpObjectAggregator(1024 * 1024 * 1024));
                            //This is the handler for websocket, provided by Netty and can also be customized. It is recommended to use the default one
                            pipeline.addLast(new WebSocketServerProtocolHandler("/" + contextPath,null,true,65535));
                            //Customized handler to handle messages sent from the server
                            pipeline.addLast(new NettyWebSocketServerHandler(nettyWebsocketMessageEvent));
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            channelFuture.channel().closeFuture().sync();
            logger.info("Netty service [{}/{}] has started!", port, contextPath);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            logger.info("Netty service [{}/{}] has been shut down!", port, contextPath);
        }
    }
}
