package vip.sujianfeng.websocket.client;

import java.util.concurrent.CountDownLatch;

public class NettyWebSocketContext {

    private CountDownLatch countDownLatch;

    private String result;

    public NettyWebSocketContext(CountDownLatch countDownLatch) {
        this.countDownLatch= countDownLatch;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch= countDownLatch;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
