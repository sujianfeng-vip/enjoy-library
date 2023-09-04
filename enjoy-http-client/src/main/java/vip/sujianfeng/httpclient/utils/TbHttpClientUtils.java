package vip.sujianfeng.httpclient.utils;

import vip.sujianfeng.httpclient.intf.IHttpResponseParser;
import vip.sujianfeng.httpclient.models.*;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import com.alibaba.fastjson.JSON;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.BodyUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author SuJianFeng
 * @date 2019/11/28 13:26
 **/
public class TbHttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(TbHttpClientUtils.class);

    private static CloseableHttpClient HTTP_CLIENT = null;
    private static IdleConnectionMonitorThread idleConnectionMonitorThread;

    private static  ConnectionKeepAliveStrategy myStrategy = (response, context) -> {
        HeaderElementIterator it = new BasicHeaderElementIterator
                (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase
                    ("timeout")) {
                return Long.parseLong(value) * 1000;
            }
        }
        return 60 * 1000;//如果没有约定，则默认定义时长为60s
    };

    private static CloseableHttpClient getHttpClient(TbHttpClientConfig clientConfig){
        if (HTTP_CLIENT == null){
             //HTTP_CLIENT = HttpClients.createDefault();

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(clientConfig.getConnectionMaxTotal()); //500
            connectionManager.setDefaultMaxPerRoute(clientConfig.getConnectionDefaultMaxPerRoute()); //例如默认每路由最高100并发，具体依据业务来定

            HTTP_CLIENT = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setKeepAliveStrategy(myStrategy)
                    //.setDefaultRequestConfig(RequestConfig.custom().setStaleConnectionCheckEnabled(true).build())
                    .build();
            idleConnectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
            idleConnectionMonitorThread.start();
            logger.info("[2020-08-28 10:07]http客户端单例初始化! {}", JSON.toJSONString(clientConfig));
        }
        return HTTP_CLIENT;
    }

    public static <T> TbHttpResponse<T> get(TbHttpClientConfig config, TbHttpGetRequest getRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            URIBuilder uriBuilder1 = createUriBuilder(getRequest);
            doHttpCall(result, new HttpGet(uriBuilder1.build()), config, getRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static <T> TbHttpResponse<T> doPost(TbHttpClientConfig config, TbHttpEntriryRequest entriryRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            HttpPost httpPost = new HttpPost(createUriBuilder(entriryRequest).build());
            doHttpCallByEntity(result, httpPost, config, entriryRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static TbHttpResponse<String> doPost(TbHttpClientConfig config, TbHttpEntriryRequest entriryRequest) {
        return doPost(config, entriryRequest, new HttpResponseStringParser());
    }

    public static <T> TbHttpResponse<T> doPost(TbHttpClientConfig config, TbHttpRequestForm entriryRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            HttpPost httpPost = new HttpPost(createUriBuilder(entriryRequest).build());
            doHttpCallByEntity(result, httpPost, config, entriryRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static <T> TbHttpResponse<T> doPut(TbHttpClientConfig config, TbHttpEntriryRequest entriryRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            HttpPut httpPut = new HttpPut(createUriBuilder(entriryRequest).build());
            doHttpCallByEntity(result, httpPut, config, entriryRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static <T> TbHttpResponse<T> doPatch(TbHttpClientConfig config, TbHttpEntriryRequest entriryRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            HttpPatch httpPatch = new HttpPatch(createUriBuilder(entriryRequest).build());
            doHttpCallByEntity(result, httpPatch, config, entriryRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static <T> TbHttpResponse<T> doDelete(TbHttpClientConfig config, TbHttpDeleteRequest deleteRequest, IHttpResponseParser<T> parser) {
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        try {
            HttpDelete httpDelete = new HttpDelete(createUriBuilder(deleteRequest).build());
            doHttpCall(result, httpDelete, config, deleteRequest);
        } catch (URISyntaxException e) {
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }
        return result;
    }

    public static URIBuilder createUriBuilder(TbHttpRequest tbHttpRequest) throws URISyntaxException {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(tbHttpRequest.getUrl());
        Map<String, String> params = tbHttpRequest.getParams();
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder;
    }

    public static <T> void doHttpCall(TbHttpResponse<T> result, HttpRequestBase httpRequestBase, TbHttpClientConfig config, TbHttpRequest httpRequest) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            // 创建httpClient对象
            httpClient = getHttpClient(config);
            /**
             * setConnectTimeout：设置连接超时时间，单位毫秒。
             * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
             * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
             * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
             */
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getConnectTimeout())
                    .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                    .setSocketTimeout(config.getSocketTimeout()).build();
            httpRequestBase.setConfig(requestConfig);
            // 设置请求头
            packageHeader(httpRequest.getHeaders(), httpRequestBase);
            httpResponse = getHttpClientResult(httpClient, httpRequestBase, result);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
    }

    public static <T> void doHttpCallByEntity(TbHttpResponse<T> result, HttpEntityEnclosingRequestBase HttpEntriryRequest, TbHttpClientConfig config, TbHttpEntriryRequest entriryRequest) {
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = null;
        try {
            // 创建httpClient对象
            httpClient = getHttpClient(config);
            /**
             * setConnectTimeout：设置连接超时时间，单位毫秒。
             * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
             * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
             * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
             */
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getConnectTimeout())
                    .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                    .setSocketTimeout(config.getSocketTimeout()).build();
            HttpEntriryRequest.setConfig(requestConfig);
            //postRequest.getHeaders().put("Content-Type", "application/json");
            packageHeader(entriryRequest.getHeaders(), HttpEntriryRequest);
            String bodyContent;
            if (entriryRequest.getBodyObj() == null){
                bodyContent = "{}";
            } else if (BodyUtils.isPrimitiveDataTypes(entriryRequest.getBodyObj())){
                bodyContent = ConvertUtils.cStr(entriryRequest.getBodyObj());
            } else {
                bodyContent = JSON.toJSONString(entriryRequest.getBodyObj());
            }
            //封装请求body数据
            HttpEntriryRequest.setEntity(new StringEntity(bodyContent, "UTF-8"));
            // 执行请求并获得响应结果
            httpResponse = getHttpClientResult(httpClient, HttpEntriryRequest, result);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
    }

    /**
     * 使用httpclint 发送文件
     * @author: SuJianFeng
     * @date: 2019-05-27
     * @param request
     *            上传的文件
     * @return 响应结果
     */
    public static <T> TbHttpResponse<T>  postFiles(TbHttpClientConfig config, TbHttpPostFilesRequest request, IHttpResponseParser<T> parser) {
        CloseableHttpClient httpClient = getHttpClient(config);
        HttpPost httpPost = new HttpPost(request.getUrl());
        TbHttpResponse<T> result = new TbHttpResponse<>(parser);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try{
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getConnectTimeout())
                    .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                    .setSocketTimeout(config.getSocketTimeout()).build();
            httpPost.setConfig(requestConfig);
            //添加header
            for (Map.Entry<String, String> e : request.getHeaders().entrySet()) {
                httpPost.addHeader(e.getKey(), e.getValue());
            }
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(StandardCharsets.UTF_8);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
            for (Map.Entry<String, InputStreamBody> entry : request.getFileMap().entrySet()) {
                //FileInputStream fileInputStream = new FileInputStream(entry.getValue());
                //InputStreamBody inputStreamBody = new InputStreamBody(fileInputStream, ContentType.MULTIPART_FORM_DATA, entry.getValue().getName());
                builder.addPart(entry.getKey(), entry.getValue());// 文件流
            }
            for (Map.Entry<String, String> e : request.getFormParams().entrySet()) {
                builder.addTextBody(e.getKey(), e.getValue());// 类似浏览器表单提交，对应input的name和value
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            httpResponse = TbHttpClientUtils.getHttpClientResult(httpClient, httpPost, result);
        } catch (Exception e){
            logger.error(e.toString(), e);
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setError(e.toString());
        }finally {
            TbHttpClientUtils.release(httpResponse, httpClient);
        }
        return result;
    }

    /**
     * Description: 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static <T> CloseableHttpResponse getHttpClientResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod, TbHttpResponse<T> tbHttpResponse) {
        CloseableHttpResponse response = null;
        // 执行请求
        try {
            response = httpClient.execute(httpMethod);
            tbHttpResponse.setResponse(response);
            // 获取返回结果
            if (response != null && response.getStatusLine() != null) {
                if (response.getEntity() != null) {
                    tbHttpResponse.setContent(tbHttpResponse.getParser().parser(response));
                }
                tbHttpResponse.setCode(response.getStatusLine().getStatusCode());
                return response;
            }
            tbHttpResponse.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            tbHttpResponse.setError("Internal Server Error");
        } catch (IOException e) {
            logger.error(e.toString(), e);
            tbHttpResponse.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            tbHttpResponse.setError(e.toString());
        }
        return response;
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        //使用单例执行，就不需要关闭资源了
        /*
        try {
            // 释放资源
            if (httpResponse != null) {
                httpResponse.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        */
    }

}
