package vip.sujianfeng.httpclient;

import vip.sujianfeng.httpclient.models.TbHttpClientConfig;
import vip.sujianfeng.httpclient.models.TbHttpPostFilesRequest;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.httpclient.parser.HttpResponseStringParser;
import vip.sujianfeng.httpclient.utils.TbHttpClientUtils;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2020/1/6 14:28
 **/
public class TbHttpPostFiles {

    private TbHttpClientConfig httpConfig;

    public TbHttpPostFiles() {
        this.httpConfig = new TbHttpClientConfig();
    }

    public TbHttpResponse<String> postFile(String url, Map<String, String> headers, Map<String, String> formParams, Map<String, File> fileMap) throws FileNotFoundException {
        TbHttpPostFilesRequest request = TbHttpPostFilesRequest.instance(url, headers, formParams, fileMap);
        return TbHttpClientUtils.postFiles(httpConfig, request, new HttpResponseStringParser());
    }

    public TbHttpResponse<String> post(String url, Map<String, String> headers, Map<String, String> formParams, Map<String, InputStreamBody> fileMap) {
        TbHttpPostFilesRequest request = new TbHttpPostFilesRequest(url, headers, formParams, fileMap);
        return TbHttpClientUtils.postFiles(httpConfig, request, new HttpResponseStringParser());
    }

    public TbHttpClientConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(TbHttpClientConfig httpConfig) {
        this.httpConfig = httpConfig;
    }
}
