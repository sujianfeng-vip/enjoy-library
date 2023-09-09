package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpEntriryRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseBytesParser;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:39
 **/
public class TbHttpPost {

    private TbHttpClientConfig httpConfig;

    public TbHttpPost() {
        this.httpConfig = new TbHttpClientConfig();
    }

    public TbHttpResponse<String> post(String url, Map<String, String> headers, Map<String, String> params, Object bodyObj, String contentType) {
        TbHttpEntriryRequest okHttpPostRequest = new TbHttpEntriryRequest(url, headers, params, bodyObj, contentType);
        return TbHttpClientUtils.doPost(httpConfig, okHttpPostRequest, new HttpResponseStringParser());
    }

    public TbHttpResponse<byte[]> postForByteResult(String url, Map<String, String> headers, Map<String, String> params, Object bodyObj, String contentType) {
        TbHttpEntriryRequest okHttpPostRequest = new TbHttpEntriryRequest(url, headers, params, bodyObj, contentType);
        return TbHttpClientUtils.doPost(httpConfig, okHttpPostRequest, new HttpResponseBytesParser());
    }

    public TbHttpResponse<String> post(String url, Map<String, String> headers, Object bodyObj) {
        return post(url, headers, new HashMap<>(), bodyObj, TbHttpEntriryRequest.CONTENT_TYPE_APPLICATION_JSON);
    }

    public TbHttpResponse<String> post(String url, Object bodyObj) {
        return post(url, null, bodyObj);
    }

    public TbHttpResponse<String> post(String url) {
        return post(url, null, null);
    }

    public TbHttpClientConfig getTbHttpClientConfig() {
        return httpConfig;
    }

    public void setTbHttpClientConfig(TbHttpClientConfig okHttpConfig) {
        this.httpConfig = okHttpConfig;
    }
}
