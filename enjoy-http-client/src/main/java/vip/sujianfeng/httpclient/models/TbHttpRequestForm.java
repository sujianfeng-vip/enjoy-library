package vip.sujianfeng.httpclient.models;

import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/11/29 7:46
 **/
public class TbHttpRequestForm extends TbHttpEntriryRequest {

    public TbHttpRequestForm(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> formBody) {
        super(url, headers, params, formBody, "CONTENT_TYPE_X_WWW_FORM_URL_ENCODED");
    }

}
