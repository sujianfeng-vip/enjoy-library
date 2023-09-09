package vip.sujianfeng.httpclient;

import cc.twobears.library.httpclient.models.*;
import vip.sujianfeng.httpclient.models.*;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/28 14:15
 **/
public class TbHttpUtilsTest {

    @Test
    public void test() throws IOException {
        TbHttpGet tbHttpGet = new TbHttpGet();
        TbHttpResponse<String> okHttpResponse = tbHttpGet.get("https://mms-test.keytop.cn/ms-mng/healthCheck");
        Object data = okHttpResponse.parserContent(okHttpResponse.getContent(), Map.class);
        System.out.println(JSON.toJSONString(data));
    }

    @Test
    public void testMethod(){
        testMethod("http://127.0.0.1:8080/rest1/testMethod?u1=u111", TbHttpEntriryRequest.CONTENT_TYPE_X_WWW_FORM_URL_ENCODED);
        testMethod("http://127.0.0.1:8080/rest2/testMethod?u1=u111", TbHttpEntriryRequest.CONTENT_TYPE_APPLICATION_JSON);
    }

    private void testMethod(String url, String contentType){
        System.out.println("============================================");
        System.out.println(url);
        System.out.println(contentType);
        Map<String, String> params = new HashMap<>();
        params.put("p1", "p111");
        TbHttpClientConfig config = new TbHttpClientConfig();
        TbHttpGetRequest getRequest = new TbHttpGetRequest(url, new HashMap<>(), params);
        TbHttpResponse<String> httpResponse = TbHttpClientUtils.get(config, getRequest, new HttpResponseStringParser());
        printResponse("get", httpResponse);

        Map<String, Object> body = new HashMap<>();
        body.put("b1", "b111");
        TbHttpEntriryRequest entriryRequest = new TbHttpEntriryRequest(url, new HashMap<>(), params, body, contentType);
        httpResponse = TbHttpClientUtils.doPost(config, entriryRequest);
        printResponse("post", httpResponse);

        entriryRequest = new TbHttpEntriryRequest(url, new HashMap<>(), params, body, contentType);
        httpResponse = TbHttpClientUtils.doPut(config, entriryRequest, new HttpResponseStringParser());
        printResponse("put", httpResponse);

        entriryRequest = new TbHttpEntriryRequest(url, new HashMap<>(), params, body, contentType);
        httpResponse = TbHttpClientUtils.doPatch(config, entriryRequest, new HttpResponseStringParser());
        printResponse("patch", httpResponse);

        TbHttpDeleteRequest deleteRequest = new TbHttpDeleteRequest(url, new HashMap<>(), params);
        httpResponse = TbHttpClientUtils.doDelete(config, deleteRequest, new HttpResponseStringParser());
        printResponse("delete", httpResponse);
    }

    @Test
    public void test2(){
        String url = "http://127.0.0.1:8080/rest2/testMethod?u1=u111";
        TbHttpClientConfig config = new TbHttpClientConfig();
        Map<String, String> params = new HashMap<>();
        params.put("p1", "b111");
        Map<String, Object> body = new HashMap<>();
        body.put("b1", "b111");
        TbHttpEntriryRequest entriryRequest = new TbHttpEntriryRequest(url, new HashMap<>(), params, body, TbHttpEntriryRequest.CONTENT_TYPE_APPLICATION_JSON);
        TbHttpResponse httpResponse = TbHttpClientUtils.doPost(config, entriryRequest);
        printResponse("post", httpResponse);
    }



    private void printResponse(String head, TbHttpResponse httpResponse){
        System.out.println(String.format("%s - > %s/%s", head, httpResponse.getCode(), httpResponse.getContent()));
    }
}
