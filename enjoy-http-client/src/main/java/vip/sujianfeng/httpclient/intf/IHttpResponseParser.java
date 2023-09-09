package vip.sujianfeng.httpclient.intf;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * author SuJianFeng
 * createTime  2022/5/11
 */
public interface IHttpResponseParser<T> {
    T parser(CloseableHttpResponse response) throws IOException;
}
