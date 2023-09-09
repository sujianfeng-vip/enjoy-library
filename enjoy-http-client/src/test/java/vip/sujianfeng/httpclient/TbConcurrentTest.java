package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpGetRequest;
import vip.sujianfeng.httpclient.models.TbHttpPostRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author SuJianFeng
 * createTime  2020/8/27 18:20
 **/
public class TbConcurrentTest {

    private static Map<Integer, Long> TIME_CONSUME = new ConcurrentHashMap<>();

    private static ThreadPoolExecutor ThreadWorker = new ThreadPoolExecutor(
            500, //正式工数量,
            1000, // 工人数量上限，包括正式工和临时工
            10, TimeUnit.SECONDS, // 临时工游手好闲的最长时间，超过这个时间将被解雇
            new ArrayBlockingQueue<>(100), // 使用有界队列，避免OOM

            // 拒单策略:
            // AbortPolicy 抛出RejectedExecutionException
            // DiscardPolicy 什么也不做，直接忽略
            // DiscardOldestPolicy 丢弃执行队列中最老的任务，尝试为当前提交的任务腾出位置
            // CallerRunsPolicy 直接由提交任务者执行这个任务
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public void testPool(int total) throws InterruptedException {
        TIME_CONSUME.clear();
        long beginTimeOut = System.currentTimeMillis();
        TbHttpGetRequest getRequest = new TbHttpGetRequest("http://127.0.0.1:8080/ms-demo/healthCheck", new HashMap<>(), new HashMap<>());
        TbHttpPostRequest postRequest = new TbHttpPostRequest("http://127.0.0.1:8080/ms-demo/postTest", new HashMap<>(), new HashMap<>(), new HashMap<>());
        TbHttpClientConfig config = new TbHttpClientConfig();
        config.setSocketTimeout(20 * 1000);
        config.setConnectTimeout(20 * 1000);
        //config.setConnectionRequestTimeout(20 * 1000);
        //config.setConnectionMaxTotal(500);
        //config.setConnectionDefaultMaxPerRoute(100);
        TbHttpClientUtils.get(config, getRequest);
        for (int i = 0; i < total; i++) {
            int iValue = i;
            ThreadWorker.execute(()->{
                long beginTime = System.currentTimeMillis();
                TbHttpResponse httpResponse = TbHttpClientUtils.doPost(config, postRequest);
                long endTime = System.currentTimeMillis() - beginTime;
                TIME_CONSUME.put(iValue, endTime);
                System.out.println(String.format("第%s次调用，耗时%s毫秒 => %s/%s", iValue, endTime, httpResponse.getCode(), httpResponse.getContent()));
            });
        }
        while (TIME_CONSUME.size() < total){
            Thread.sleep(10);
        }
        long totalTime = 0;
        for (Map.Entry<Integer, Long> entry : TIME_CONSUME.entrySet()) {
            totalTime += entry.getValue();
        }
        System.out.println(String.format("ApacheHttp: 并发%s毫秒, 累计调用%s次，累计耗时%s毫秒, 平均耗时%s毫秒!",
                System.currentTimeMillis() - beginTimeOut, total, totalTime, totalTime / total));
    }

    @Test
    public void test() throws InterruptedException {
        testPool(100);
        testPool(100);
        testPool(100);
        callMMS();
        while (true){
            Thread.sleep(1000);
        }
    }

    @Test
    public void timeoutTest(){
        TbHttpPostRequest postRequest = new TbHttpPostRequest("http://127.0.0.1:8080/ms-demo/postTest", new HashMap<>(), new HashMap<>(), new HashMap<>());
        TbHttpClientConfig config = new TbHttpClientConfig();
        config.setSocketTimeout(6 * 1000);
        config.setConnectTimeout(6 * 1000);
        TbHttpResponse httpResponse = TbHttpClientUtils.doPost(config, postRequest);
        System.out.println(String.format("%s/%s", httpResponse.getCode(), httpResponse.getContent()));
    }

    private String registerMasterUrl = "https://mms-register-master.keytop.cn/register-master/";

    @Test
    public void callMMS(){
        TbHttpGetRequest getRequest = new TbHttpGetRequest(registerMasterUrl, new HashMap<>(), new HashMap<>());
        TbHttpClientConfig config = new TbHttpClientConfig();
        TbHttpResponse httpResponse = TbHttpClientUtils.get(config, getRequest);
        System.out.println(String.format("%s/%s", httpResponse.getCode(), httpResponse.getContent()));
    }


}
