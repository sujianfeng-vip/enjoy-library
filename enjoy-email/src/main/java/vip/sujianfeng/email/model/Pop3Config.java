package vip.sujianfeng.email.model;

public class Pop3Config {
    private String port;
    private String host;
    private String user;
    private String password;
    private boolean useSsl;

    public static Pop3Config get163mailConfig(String user, String password){
        return new Pop3Config("110", "pop3.163.com", user, password);
    }

    public Pop3Config() {
    }

    public Pop3Config(String port, String host, String user, String password) {
        this.port = port;
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }
}
