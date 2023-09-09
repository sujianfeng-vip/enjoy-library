package vip.sujianfeng.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.websocket.intf.NettyWebSocketMessageEvent;

import java.net.URI;
import java.net.URISyntaxException;

public class NettyWebSocketClient extends AbstractNettyWebsocketClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketClient.class);

    private static final NioEventLoopGroup NIO_GROUP = new NioEventLoopGroup();

    private final URI uri;

    private final int port;

    private Bootstrap bootstrap;

    private NettyWebSocketClientHandler handler;

    private Channel channel;

    private NettyWebSocketMessageEvent nettyWebsocketMessageEvent;

    public NettyWebSocketClient(String url, int connectionTimeout, NettyWebSocketMessageEvent nettyWebsocketMessageEvent) throws URISyntaxException, MyException {
        super(connectionTimeout);
        this.uri = new URI(url);
        this.port = getPort();
        this.nettyWebsocketMessageEvent = nettyWebsocketMessageEvent;
    }

    /**
     * Extract the specified port
     *
     * @return the specified port or the default port for the specific scheme
     */
    private int getPort() throws MyException {
        int port = uri.getPort();
        if (port == -1) {
            String scheme = uri.getScheme();
            if ("wss".equals(scheme)) {
                return 443;
            } else if ("ws".equals(scheme)) {
                return 80;
            }
        }
        return port;
    }

    @Override
    protected void doOpen() {
        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        handler = new NettyWebSocketClientHandler(webSocketClientHandshaker, this.nettyWebSocketContext, this.nettyWebsocketMessageEvent);
        bootstrap = new Bootstrap();
        bootstrap.group(NIO_GROUP).channel(NioSocketChannel.class).handler(new NettyWebSocketChannelInitializer(handler));
    }

    @Override
    protected void doConnect() {
        try {
            logger.info("NettyWebSocketClient[{}:{}]connecting....", uri.getHost(), port);
            channel = bootstrap.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();
            logger.info("NettyWebSocketClient [{}:{}] connected ok!", uri.getHost(), port);
        } catch (InterruptedException e) {
            logger.error("websocket connect error!", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected Channel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        if (channel != null) {
            channel.close();
        }
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

}