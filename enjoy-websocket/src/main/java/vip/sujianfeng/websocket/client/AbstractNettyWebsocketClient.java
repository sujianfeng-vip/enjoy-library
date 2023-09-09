package vip.sujianfeng.websocket.client;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNettyWebsocketClient implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNettyWebsocketClient.class);

    private final int connectionTimeout;

    protected NettyWebSocketContext nettyWebSocketContext;

    public AbstractNettyWebsocketClient(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.nettyWebSocketContext = new NettyWebSocketContext(new CountDownLatch(1));
    }

    public void send(String message) throws MyException {
        Channel channel = getChannel();
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
            return;
        }
        throw new MyException ("The connection has been closed");
    }

    public void connect() throws MyException {
        try {
            doOpen();
            doConnect();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public String receiveResult() throws MyException {
        this.receive(this.nettyWebSocketContext.getCountDownLatch());
        if (StringUtils.isEmpty(this.nettyWebSocketContext.getResult())) {
            throw new MyException("Task result information not obtained");
        }
        return this.nettyWebSocketContext.getResult();
    }

    private void receive(CountDownLatch countDownLatch) throws MyException {
        boolean waitFlag = false;
        try {
            waitFlag = countDownLatch.await(connectionTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.toString(), e);
            Thread.currentThread().interrupt();
        }
        if (!waitFlag) {
            logger.error("Timeout({}s) when receiving response message", connectionTimeout);
            throw new MyException ("This connection did not receive response information");
        }
    }

    protected abstract void doOpen();

    protected abstract void doConnect() throws MyException;

    protected abstract Channel getChannel();

    @Override
    public abstract void close();
}