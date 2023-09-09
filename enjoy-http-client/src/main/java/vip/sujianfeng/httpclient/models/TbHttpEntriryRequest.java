package vip.sujianfeng.httpclient.models;

import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:46
 **/
public class TbHttpEntriryRequest extends TbHttpRequest {

    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE_X_WWW_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    private Object bodyObj;

    /**
     *
     * @param url
     * @param headers
     * @param bodyObj
     * @param contentType -> application/json  or  application/x-www-form-urlencoded
     */
    public TbHttpEntriryRequest(String url, Map<String, String> headers, Map<String, String> params, Object bodyObj, String contentType) {
        super(url, headers, params);
        this.bodyObj = bodyObj;
        this.getHeaders().put("Content-Type", contentType);
    }

    public Object getBodyObj() {
        return bodyObj;
    }

    public void setBodyObj(Object bodyObj) {
        this.bodyObj = bodyObj;
    }

}
