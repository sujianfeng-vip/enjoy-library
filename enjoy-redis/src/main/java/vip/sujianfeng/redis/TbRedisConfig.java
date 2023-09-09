package vip.sujianfeng.redis;

/**
 * author sujianfeng
 * @create 2019-10-17 6:18
 */
public class TbRedisConfig {
    private String host;
    private int port;
    private String password;
    private int database;
    private int timeCount = 5000;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(int timeCount) {
        this.timeCount = timeCount;
    }
}
