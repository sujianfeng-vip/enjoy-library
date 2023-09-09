package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpGetRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;

import java.io.IOException;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:39
 **/
public class TbHttpGet {

    private TbHttpClientConfig httpConfig;

    public TbHttpGet() {
        this.httpConfig = new TbHttpClientConfig();
    }

    public TbHttpResponse<String> get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        TbHttpGetRequest okHttpGetRequest = new TbHttpGetRequest(url, headers, params);
        return TbHttpClientUtils.get(httpConfig, okHttpGetRequest, new HttpResponseStringParser());
    }

    public TbHttpResponse<String> get(String url, Map<String, String> params) throws IOException {
        return get(url, null, params);
    }

    public TbHttpResponse<String> get(String url) throws IOException {
        return get(url, null, null);
    }

    public TbHttpClientConfig getTbHttpClientConfig() {
        return httpConfig;
    }

    public void setTbHttpClientConfig(TbHttpClientConfig okHttpConfig) {
        this.httpConfig = okHttpConfig;
    }
}
