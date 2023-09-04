package vip.sujianfeng.httpclient.models;

import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2019/11/29 7:46
 **/
public class TbHttpGetRequest extends TbHttpRequest {
    public TbHttpGetRequest(String url, Map<String, String> headers, Map<String, String> params) {
        super(url, headers, params);
    }
}
