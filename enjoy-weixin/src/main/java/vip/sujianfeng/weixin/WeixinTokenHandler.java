package vip.sujianfeng.weixin;

import vip.sujianfeng.utils.cache.DataCacheHandler;
import vip.sujianfeng.weixin.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

/**
 * Created by sujianfeng on 2016/7/29.
 */
public class WeixinTokenHandler {
    private static Logger logger = LoggerFactory.getLogger(WeixinTokenHandler.class);
    private String weixinAppId;
    private String weixinSecret;
    private DataCacheHandler dataCacheHandler;

    private static final int TIMEOUT_MINUTES = 60;

    private static final String WEIXIN_TOKEN_ACCESS_TOKEN = "WEIXIN_ACCESS_TOKEN_";
    private static final String WEIXIN_TOKEN_JS_API_TICKET = "WEIXIN_JS_API_TICKET_";

    private synchronized String get_ACCESS_TOKEN(){
        String obj = null;
        try {
            obj = dataCacheHandler.getOrBuildCacheObj(WEIXIN_TOKEN_ACCESS_TOKEN + weixinAppId, String.class, TIMEOUT_MINUTES, () -> {
                String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", weixinAppId, weixinSecret);
                String resultJson = HttpClientUtil.sendHttpRequestGet(url, "UTF-8", "GET");
                JSONObject resultMap = JSON.parseObject(resultJson, JSONObject.class);
                String result = ConvertUtils.cStr(resultMap.get("access_token"));
                if (StringUtilsEx.isEmpty(result)){
                    logger.info("===========================================");
                    logger.info(resultJson);
                    logger.info("=====获取ACCESS_TOKEN失败===================");
                    return null;
                }
                return result;
            });
        } catch (Exception e) {
            logger.error("获取ACCESS_TOKEN失败 => ", e);
        }
        return obj;
    }

    private void set_ACCESS_TOKEN(String access_token){
        dataCacheHandler.addCache(WEIXIN_TOKEN_ACCESS_TOKEN + weixinAppId, access_token, TIMEOUT_MINUTES);
    }

    private String get_JS_API_TICKET(){
        String obj = null;
        try {
            obj = dataCacheHandler.getOrBuildCacheObj(WEIXIN_TOKEN_JS_API_TICKET + weixinAppId, String.class, TIMEOUT_MINUTES, () -> {
                String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", this.get_ACCESS_TOKEN());
                String resultJson = HttpClientUtil.sendHttpRequestGet(url, "UTF-8", "GET");
                JSONObject resultMap = JSON.parseObject(resultJson, JSONObject.class);
                String result = ConvertUtils.cStr(resultMap.get("ticket"));
                if (StringUtilsEx.isEmpty(result)){
                    logger.info("===========================================");
                    logger.info(resultJson);
                    logger.info("=====获取JS_API_TICKET失败==================");
                    return null;
                }
                return result;
            });
        } catch (Exception e) {
            logger.error("获取JS_API_TICKET失败 =>", e);
        }
        return obj;
    }

    public void clearTokenCache() {
        dataCacheHandler.removeCache(WEIXIN_TOKEN_JS_API_TICKET + weixinAppId);
        dataCacheHandler.removeCache(WEIXIN_TOKEN_ACCESS_TOKEN + weixinAppId);
    }


    public String require_ACCESS_TOKEN(){
        return get_ACCESS_TOKEN();
    }

    public String require_JS_API_TICKET(){
        return get_JS_API_TICKET();
    }

    public static void main(String args[]){
        /*
        set_LAST_REQUEST_TIME(new Date());
        Date last_request_time = get_LAST_REQUEST_TIME();
        System.out.println(DateTimeUtils.datetime2longShow(last_request_time));
        */
        //require_ACCESS_TOKEN();
    }

    public String getWeixinAppId() {
        return weixinAppId;
    }

    public void setWeixinAppId(String weixinAppId) {
        this.weixinAppId = weixinAppId;
    }

    public String getWeixinSecret() {
        return weixinSecret;
    }

    public void setWeixinSecret(String weixinSecret) {
        this.weixinSecret = weixinSecret;
    }

    public DataCacheHandler getDataCacheHandler() {
        return dataCacheHandler;
    }

    public void setDataCacheHandler(DataCacheHandler dataCacheHandler) {
        this.dataCacheHandler = dataCacheHandler;
    }
}
