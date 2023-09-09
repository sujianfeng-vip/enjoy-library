package vip.sujianfeng.httpclient.models;

import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:44
 **/
public class TbHttpRequest {
    private String url;
    private Map<String, String> params;
    private Map<String, String> headers;

    public TbHttpRequest(String url, Map<String, String> headers, Map<String, String> params) {
        this.url = url;
        this.headers = headers != null ? headers : new HashMap<>();
        this.params = params != null ? params : new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
