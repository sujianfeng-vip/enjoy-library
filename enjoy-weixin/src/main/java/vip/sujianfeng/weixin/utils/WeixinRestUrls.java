package vip.sujianfeng.weixin.utils;

import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static vip.sujianfeng.weixin.utils.Const.WEIXIN_AUTH_STATE_BASE;
import static vip.sujianfeng.weixin.utils.Const.WEIXIN_AUTH_STATE_USER_INFO;

/**
 * @Author 苏建锋
 * @create 2019-01-19 12:04
 */
public class WeixinRestUrls {
    private static Logger logger = LoggerFactory.getLogger(WeixinRestUrls.class);
    public static String getWeixinCodeUrl(String weixinAppId, String state, String scope, String redirect_uri){
        String encodeUrl = redirect_uri;
        try {
            encodeUrl = URLEncoder.encode(redirect_uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString(), e);
        }
        StringBuilderEx weixinUrl = new StringBuilderEx();
        weixinUrl.append("https://open.weixin.qq.com/connect/oauth2/authorize");
        weixinUrl.append("?appid=").append(weixinAppId);
        weixinUrl.append("&redirect_uri=").append(encodeUrl);
        weixinUrl.append("&response_type=code");
        weixinUrl.append("&scope=").append(scope);
        weixinUrl.append("&state=").append(state).append("#wechat_redirect");
        return weixinUrl.toString();
    }

    /**
     * 重定向到个人微信授权
     */
    public static String redirectWeixinAuth(String weixinAppId, String currFullUrl, String code, String state) {
        String redirect_uri = StringUtilsEx.leftStr(currFullUrl, "?");
        Map<String, String> map = urlParams2map(currFullUrl);
        map.remove("code");
        map.remove("state");
        redirect_uri += map2urlParams(map);
        String url;
        if (isWeixinCallBack_base(code, state)){
            url = getWeixinAuthCodeUrl_snsapi_userinfo(weixinAppId, WEIXIN_AUTH_STATE_USER_INFO, redirect_uri);
        }else{
            url = getWeixinAuthCodeUrl_snsapi_base(weixinAppId, WEIXIN_AUTH_STATE_BASE, redirect_uri);
        }
        return url;
    }

    public static String redirectWeixinAuth_userInfo(String weixinAppId, String currFullUrl) {
        String redirect_uri = StringUtilsEx.leftStr(currFullUrl, "?");
        Map<String, String> map = urlParams2map(currFullUrl);
        map.remove("code");
        map.remove("state");
        redirect_uri += map2urlParams(map);
        return getWeixinAuthCodeUrl_snsapi_userinfo(weixinAppId, WEIXIN_AUTH_STATE_USER_INFO, redirect_uri);
    }

    public static Map<String, String> urlParams2map(String url){
        if (url.contains("?")){
            url = StringUtilsEx.rightStr(url, "?");
            return string2map(url, "&", "=");
        }
        return new HashMap<>();
    }

    public static String map2urlParams(Map<String, String> map){
        if (map.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(sb.length() == 0 ? "?" : "&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public static Map<String, String> string2map(String str, String split1, String split2){
        Map<String, String> result = new HashMap<>();
        if (StringUtilsEx.isEmpty(str)){
            return result;
        }
        String[] params = str.split(split1);
        for(String param: params){
            String[] paramPair = param.split(split2);
            if (paramPair.length > 1){
                result.put(paramPair[0].trim(), paramPair[1]);
            }
        }
        return result;
    }

    public static boolean isWeixinCallBack_base(String code, String state){
        return StringUtilsEx.isNotEmpty(code) && StringUtilsEx.sameText(state, WEIXIN_AUTH_STATE_BASE);
    }

    /**
     * 获取（授权范围为snsapi_base的code）url
     * @param weixinAppId
     * @param state
     * @param redirect_uri
     * @return
     */
    public static String getWeixinAuthCodeUrl_snsapi_base(String weixinAppId, String state, String redirect_uri){
        return getWeixinCodeUrl(weixinAppId, state, "snsapi_base", redirect_uri);
    }


    /**
     * 获取（授权范围为snsapi_userinfo的code）url
     * @param weixinAppId
     * @param state
     * @param redirect_uri
     * @return
     */
    public static String getWeixinAuthCodeUrl_snsapi_userinfo(String weixinAppId, String state, String redirect_uri){
        return getWeixinCodeUrl(weixinAppId, state, "snsapi_userinfo", redirect_uri);
    }

    /**
     * 通过code换取accessToken的url
     * @param weixinAppId
     * @param weixinSecretId
     * @param code
     * @return
     */
    public static String getWeixinAccessTockenUrl(String weixinAppId, String weixinSecretId, String code){
        StringBuilderEx weixinUrl = new StringBuilderEx();
        weixinUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token");
        weixinUrl.append("?appid=").append(weixinAppId);
        weixinUrl.append("&secret=").append(weixinSecretId);
        weixinUrl.append("&code=").append(code);
        weixinUrl.append("&grant_type=authorization_code");
        return weixinUrl.toString();
    }

    /**
     * 通过accessToken获取用户信息的url
     * 这个accessToken必须为snsapi_userinfo换取
     * @param accessTocken
     * @param openId
     * @return
     */
    public static String getWeixinUserInfoUrl(String accessTocken, String openId){
        String weixinUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",
                accessTocken, openId);
        return weixinUrl;
    }
}
