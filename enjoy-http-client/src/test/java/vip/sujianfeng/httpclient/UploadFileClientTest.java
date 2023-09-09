package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpPostFilesRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2020/1/6 9:36
 **/
public class UploadFileClientTest {

    @Test
    public void test() throws IOException {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("param1", "111");
        File file1 = new File("C:\\Temp\\中文图片文件1.jpg");
        File file2 = new File("C:\\Temp\\中文图片文件2.jpg");

        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("file1", file1);
        fileMap.put("file2", file2);

        TbHttpPostFilesRequest request = TbHttpPostFilesRequest.instance("http://127.0.0.1:8080/ms-demo/upload", new HashMap<>(), formParams, fileMap);
        TbHttpResponse<String> httpResponse = TbHttpClientUtils.postFiles(new TbHttpClientConfig(), request, new HttpResponseStringParser());
        System.out.println(JSON.toJSONString(httpResponse));
    }
}
