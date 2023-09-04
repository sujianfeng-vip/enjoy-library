package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpEntriryRequest;
import vip.sujianfeng.httpclient.models.TbHttpGetRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SuJianFeng
 * @date 2020/6/15 10:43
 **/
public class TbHttpClientUtilsTest {
    private static Logger logger = LoggerFactory.getLogger(TbHttpClientUtilsTest.class);
    public static ThreadPoolExecutor ThreadWorker100 = new ThreadPoolExecutor(
            50, //正式工数量,
            100, // 工人数量上限，包括正式工和临时工
            10, TimeUnit.SECONDS, // 临时工游手好闲的最长时间，超过这个时间将被解雇
            new ArrayBlockingQueue<>(100), // 使用有界队列，避免OOM

            // 拒单策略:
            // AbortPolicy 抛出RejectedExecutionException
            // DiscardPolicy 什么也不做，直接忽略
            // DiscardOldestPolicy 丢弃执行队列中最老的任务，尝试为当前提交的任务腾出位置
            // CallerRunsPolicy 直接由提交任务者执行这个任务
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Test
    public void test() {
        TbHttpClientConfig httpConfig = new TbHttpClientConfig();
        for (int i = 0; i < 100; i++) {
            final int tmp = i;
            ThreadWorker100.execute(()->{
                String url = "https://mms-test.keytop.cn/ms-mng/msServiceTest/getTest3?param1=" + tmp;
                TbHttpGetRequest httpRequest = new TbHttpGetRequest(url, null, null);
                TbHttpResponse<String> httpResponse = TbHttpClientUtils.get(httpConfig, httpRequest, new HttpResponseStringParser());
                int param1 = JSON.parseObject(httpResponse.getContent()).getJSONObject("data").getInteger("param1");
                if (param1 == tmp){
                    System.out.println(String.format("ok %s -> %s", tmp, param1));
                }else{
                    System.out.println(String.format("fail %s -> %s", tmp, param1));
                }

            });
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            logger.error(e.toString(), e);
        }
    }

    @Test
    public void test2() {
        TbHttpClientConfig httpConfig = new TbHttpClientConfig();
        for (int i = 0; i < 100; i++) {
            final int tmp = i;
            ThreadWorker100.execute(()->{
                String url = "https://mms-test.keytop.cn/ms-mng/msServiceTest/postTest3?param1=" + tmp;
                Map<String, Object> body = new HashMap<>();
                body.put("param1", tmp);
                TbHttpEntriryRequest request = new TbHttpEntriryRequest(url, null, null, body, TbHttpEntriryRequest.CONTENT_TYPE_X_WWW_FORM_URL_ENCODED);
                TbHttpResponse<String> httpResponse = TbHttpClientUtils.doPost(httpConfig, request);
                int param1 = JSON.parseObject(httpResponse.getContent()).getJSONObject("data").getJSONObject("paramObj1").getInteger("param1");
                if (param1 == tmp){
                    System.out.println(String.format("ok %s -> %s", tmp, param1));
                }else{
                    System.out.println(String.format("fail %s -> %s", tmp, param1));
                }

            });
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            logger.error(e.toString(), e);
        }
    }
}
