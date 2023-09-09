package vip.sujianfeng.httpclient.models;

import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:46
 **/
public class TbHttpPostRequest extends TbHttpEntriryRequest {
    /**
     *
     * @param url
     * @param headers
     * @param bodyObj
     */
    public TbHttpPostRequest(String url, Map<String, String> headers, Map<String, String> params, Object bodyObj) {
        super(url, headers, params, bodyObj, CONTENT_TYPE_APPLICATION_JSON);
    }

}
