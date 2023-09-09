package vip.sujianfeng.httpclient.models;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:31
 * httpClient连接配置
 **/
public class TbHttpClientConfig {
    /**
     * 建立连接超时时间（毫秒)
     */
    private int connectTimeout = 10 * 1000;
    /**
     * 数据传送超时时间（毫秒）
     */
    private int socketTimeout = 15 * 1000;
    private String encoding = "UTF-8";
    /**
     * 从连接池取连接超时时间（毫秒）
     */
    private int connectionRequestTimeout = 5000;
    /**
     * 最大连接数
     */
    private int connectionMaxTotal = 500;
    /**
     * 每路由最大连接数
     */
    private int connectionDefaultMaxPerRoute = 100;

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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getConnectionMaxTotal() {
        return connectionMaxTotal;
    }

    public void setConnectionMaxTotal(int connectionMaxTotal) {
        this.connectionMaxTotal = connectionMaxTotal;
    }

    public int getConnectionDefaultMaxPerRoute() {
        return connectionDefaultMaxPerRoute;
    }

    public void setConnectionDefaultMaxPerRoute(int connectionDefaultMaxPerRoute) {
        this.connectionDefaultMaxPerRoute = connectionDefaultMaxPerRoute;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }
}
