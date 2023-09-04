package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import cc.twobears.tbcore.db.DbConfig;
import vip.sujianfeng.utils.define.CallResult;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.util.List;

/**
 * @author SuJianFeng
 * @date 2019/12/18 15:06
 **/
public class TbHttpResponseTest {

    @Test
    public void test(){
        TbHttpResponse<String> httpResponse = new TbHttpResponse<>(new HttpResponseStringParser());
        httpResponse.setContent("{}");
        TypeReference<CallResult<List<String>>> typeReference = new TypeReference<CallResult<List<String>>>(String.class) {};
        CallResult<List<String>> result = httpResponse.parserContent(httpResponse.getContent(), typeReference);
        System.out.println(result);

        String s = httpResponse.getContent();
        System.out.println(s);

        DbConfig dbConfig = httpResponse.parserContent(httpResponse.getContent(), DbConfig.class);
        System.out.println(dbConfig);
    }
}
