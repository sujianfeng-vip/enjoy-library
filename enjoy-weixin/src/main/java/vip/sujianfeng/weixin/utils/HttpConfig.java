package vip.sujianfeng.weixin.utils;

/**
 * author SuJianFeng
 * createTime  2019/7/30 13:55
 **/
public class HttpConfig {
    private String encoding = "UTF-8";
    private int connectTimeout = 10000;
    private int socketTimeout = 8000;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
