package vip.sujianfeng.email.model;


import vip.sujianfeng.utils.comm.StringUtilsEx;

public class SmtpConfig {
    private String host = "smtp.163.com";
    private String port = "25";
    private String user;
    private String password;
    private String fromAddress;
    private String fromName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        if (!StringUtilsEx.isEmpty(fromAddress)){
            return fromAddress;
        }
        return user;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
