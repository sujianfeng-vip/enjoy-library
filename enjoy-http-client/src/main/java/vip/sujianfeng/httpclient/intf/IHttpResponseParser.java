package vip.sujianfeng.httpclient.intf;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * @author SuJianFeng
 * @date 2022/5/11
 * @Description
 */
public interface IHttpResponseParser<T> {
    T parser(CloseableHttpResponse response) throws IOException;
}
