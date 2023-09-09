package vip.sujianfeng.httpclient.parser;

import vip.sujianfeng.httpclient.intf.IHttpResponseParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * author SuJianFeng
 * createTime  2022/5/11
 * @Description
 */
public class HttpResponseStringParser implements IHttpResponseParser<String> {

    @Override
    public String parser(CloseableHttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

}
