package vip.sujianfeng.httpclient.parser;

import vip.sujianfeng.httpclient.intf.IHttpResponseParser;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author SuJianFeng
 * @date 2022/5/11
 * @Description
 */
public class HttpResponseBytesParser implements IHttpResponseParser<byte[]> {

    @Override
    public byte[] parser(CloseableHttpResponse response) throws IOException {
        InputStream is = response.getEntity().getContent();
        int len = (int) response.getEntity().getContentLength();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[len];
        int rc;
        while ((rc = is.read(buff, 0, len)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

}
