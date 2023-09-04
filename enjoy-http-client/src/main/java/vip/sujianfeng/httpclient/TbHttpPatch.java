package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpEntriryRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2019/11/29 7:39
 **/
public class TbHttpPatch {

    private TbHttpClientConfig httpConfig;

    public TbHttpPatch() {
        this.httpConfig = new TbHttpClientConfig();
    }

    public TbHttpResponse<String> patch(String url, Map<String, String> headers, Map<String, String> params, Object bodyObj, String contentType) {
        TbHttpEntriryRequest okHttpPostRequest = new TbHttpEntriryRequest(url, headers, params, bodyObj, contentType);
        return TbHttpClientUtils.doPut(httpConfig, okHttpPostRequest, new HttpResponseStringParser());
    }

    public TbHttpResponse<String> patch(String url, Map<String, String> headers, Object bodyObj) {
        return patch(url, headers, new HashMap<>(), bodyObj, TbHttpEntriryRequest.CONTENT_TYPE_APPLICATION_JSON);
    }

    public TbHttpResponse<String> patch(String url, Object bodyObj) {
        return patch(url, null, bodyObj);
    }

    public TbHttpResponse<String> patch(String url) {
        return patch(url, null, null);
    }

    public TbHttpClientConfig getTbHttpClientConfig() {
        return httpConfig;
    }

    public void setTbHttpClientConfig(TbHttpClientConfig okHttpConfig) {
        this.httpConfig = okHttpConfig;
    }
}
