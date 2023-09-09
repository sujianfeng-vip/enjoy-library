package vip.sujianfeng.websocket.client;

public class MyException extends Exception {

    public MyException () {
        super();
    }

    public MyException (String message) {
        super(message);
    }

    public MyException (String message, Throwable cause) {
        super(message, cause);
    }

}
