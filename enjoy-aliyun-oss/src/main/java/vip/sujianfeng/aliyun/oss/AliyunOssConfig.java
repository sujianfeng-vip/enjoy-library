package vip.sujianfeng.aliyun.oss;

/**
 * @Author SuJianFeng
 * @Date 2019/1/22 17:47
 **/
public class AliyunOssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String backetName;
    private String ossDomain;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBacketName() {
        return backetName;
    }

    public void setBacketName(String backetName) {
        this.backetName = backetName;
    }

    public String getOssDomain() {
        return ossDomain;
    }

    public void setOssDomain(String ossDomain) {
        this.ossDomain = ossDomain;
    }
}
