package vip.sujianfeng.httpclient.models;

import vip.sujianfeng.httpclient.intf.IHttpResponseParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * @author SuJianFeng
 * @date 2019/11/28 13:45
 **/
public class TbHttpResponse<T> {

    public <A> A parserContent(String json, TypeReference<A> typeReference){
        return JSON.parseObject(json, typeReference);
    }

    public <A> A parserContent(String json, Class<A> cls){
        return JSON.parseObject(json, cls);
    }

    public TbHttpResponse(IHttpResponseParser<T> parser) {
        this.parser = parser;
    }

    private int code;
    private T content;
    private String error;
    private CloseableHttpResponse response;
    private final IHttpResponseParser<T> parser;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CloseableHttpResponse getResponse() {
        return response;
    }

    public void setResponse(CloseableHttpResponse response) {
        this.response = response;
    }

    public T getContent() {
        return content;
    }

    public IHttpResponseParser<T> getParser() {
        return parser;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
