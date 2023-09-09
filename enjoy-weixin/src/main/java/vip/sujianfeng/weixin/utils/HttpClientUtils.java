package vip.sujianfeng.weixin.utils;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

public class HttpClientUtils {

    public static HttpClientResult doGet(HttpConfig config, String url) throws Exception {
        return doGet(config, url, new HashMap<>(), new HashMap<>());
    }

    public static HttpClientResult doGet(HttpConfig config, String url, Map<String, String> params) throws Exception {
        return doGet(config, url, new HashMap<>(), params);
    }

    public static HttpClientResult doGet(HttpConfig config, String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpGet.setConfig(requestConfig);
        packageHeader(headers, httpGet);
        CloseableHttpResponse httpResponse = null;

        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpGet);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static HttpClientResult doPost(HttpConfig config, String url) throws Exception {
        return doPost(config, url, new HashMap<>(), "{}");
    }

    public static HttpClientResult doPost(HttpConfig config, String url, String bodyContent) throws Exception {
        return doPost(config, url, new HashMap<>(), bodyContent);
    }

    public static HttpClientResult doPost(HttpConfig config, String url, Map<String, String> headers, String bodyContent) throws Exception {
        return doPostRawJson(config, url, headers, bodyContent);
    }
    public static HttpClientResult doPostRawJson(HttpConfig config, String url, Map<String, String> headers, String bodyContent) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpPost.setConfig(requestConfig);
        headers.put("Content-Type", "application/json");
        packageHeader(headers, httpPost);
        httpPost.setEntity(new StringEntity(bodyContent, "UTF-8"));
        CloseableHttpResponse httpResponse = null;
        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpPost);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static HttpClientResult doPostWwwFormUrlEncoded(HttpConfig config, String url, Map<String, String> headers, Map<String, String> params) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpPost.setConfig(requestConfig);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        packageHeader(headers, httpPost);

        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), ConvertUtils.cStr(entry.getValue())));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, config.getEncoding()));

        CloseableHttpResponse httpResponse = null;

        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpPost);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static HttpClientResult doPatch(HttpConfig config, String url, Map<String, String> headers, Map<String, String> params) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPatch httpPatch = new HttpPatch(url);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpPatch.setConfig(requestConfig);

        packageHeader(headers, httpPatch);

        packageParam(config, params, httpPatch);

        CloseableHttpResponse httpResponse = null;

        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpPatch);
        } finally {

            release(httpResponse, httpClient);
        }
    }

    public static HttpClientResult doPut(String url) throws Exception {
        return doPut(url);
    }

    public static HttpClientResult doPut(HttpConfig config, String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpPut.setConfig(requestConfig);
        packageParam(config, params, httpPut);
        CloseableHttpResponse httpResponse = null;
        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpPut);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static HttpClientResult doDelete(HttpConfig config, String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build();
        httpDelete.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            return getHttpClientResult(config, httpResponse, httpClient, httpDelete);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void packageParam(HttpConfig config, Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), ConvertUtils.cStr(entry.getValue())));
            }
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, config.getEncoding()));
        }
    }
    public static HttpClientResult getHttpClientResult(HttpConfig config, CloseableHttpResponse httpResponse,
                                                       CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        httpResponse = httpClient.execute(httpMethod);
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), config.getEncoding());
            }
            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    public static String getAuthorization(String appId, String secretKey) {
        String auth = appId + ":" + secretKey;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }

}
