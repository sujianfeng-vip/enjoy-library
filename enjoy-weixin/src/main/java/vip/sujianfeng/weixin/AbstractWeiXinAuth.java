package vip.sujianfeng.weixin;

import vip.sujianfeng.utils.cache.IDataCache;
import vip.sujianfeng.weixin.model.WeixinToken;
import vip.sujianfeng.weixin.model.WeixinUser;
import vip.sujianfeng.weixin.utils.HttpClientResult;
import vip.sujianfeng.weixin.utils.HttpClientUtils;
import vip.sujianfeng.weixin.utils.HttpConfig;
import vip.sujianfeng.weixin.utils.WeixinRestUrls;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.define.CallResult;
import vip.sujianfeng.utils.comm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static vip.sujianfeng.weixin.utils.Const.WEIXIN_AUTH_STATE_BASE;
import static vip.sujianfeng.weixin.utils.Const.WEIXIN_AUTH_STATE_USER_INFO;

/**
 * Created by sujianfeng on 2016/3/22.
 */
public abstract class AbstractWeiXinAuth {

    private static Logger logger = LoggerFactory.getLogger(AbstractWeiXinAuth.class);

    protected abstract WeixinTokenHandler weixinTokenHandler();
    protected abstract void saveWeixinUser(CallResult<?> callResult, WeixinUser weixinUser) throws Exception;
    protected abstract WeixinUser getWeixinUserByOpenId(CallResult<?> callResult, String openid) throws Exception;
    protected abstract IDataCache getDataCache();


    private JSONObject getWeixinUserMap(String openId) throws Exception {
        String access_token = weixinTokenHandler().require_ACCESS_TOKEN();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                access_token, openId);
        HttpClientResult httpClientResult = HttpClientUtils.doGet(new HttpConfig(), url);
        if (httpClientResult.getCode() != 200) {
            throw new Exception(httpClientResult.getContent());
        }
        return JSON.parseObject(httpClientResult.getContent());
    }

    public boolean isSubscribe(String openId) throws Exception {
        JSONObject result = getWeixinUserMap(openId);
        return ConvertUtils.cBool(result.get("subscribe"));
    }


    private InputStream getInputStream(String mediaId, Map<String, Object> result) {
        InputStream is = null;
        String accessToken = weixinTokenHandler().require_ACCESS_TOKEN();
        //accessToken = "ouQ-KqNBQIH_TnTqL2QSM58UteNexFDVfBMP8OSi9Hq81QRy2Ufjq6fdOpdBL0XAjD9xB3STS4vhYeif1ZUq1-gUeK5SqpDZPaWsjXq2rVg";
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                + accessToken + "&media_id=" + mediaId;
        logger.info("Getting resources from WeChat server:" + mediaId);
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            http.connect();
            // Convert file to byte stream
            is = http.getInputStream();
        } catch (Exception e) {
            System.out.println("Failed to obtain WeChat image:" + e.toString());
            result.put("success", 0);
            result.put("msg", e.toString());
            logger.error(e.toString(), e);
        }
        return is;

    }
    public boolean downloadMedia(String webPath, String imgFilePath, String media_id, Map<String, Object> result) {
        //String imgFilePath = request.getSession().getServletContext().getRealPath("upload");
        //String webPath = request.getContextPath();
        return downloadMedia(1, imgFilePath, webPath, media_id, result);
    }

    private static final String JPG_EXT = ".jpg";

    public boolean downloadMedia(int tryCount, String imgFilePath, String contextPath, String media_id, Map<String, Object> result) {
        if (StringUtilsEx.isEmpty(media_id)){
            media_id = new GuidUtils().newGuid();
        }
        String fileName = media_id + JPG_EXT;
        InputStream inputStream = getInputStream(media_id, result);
        if (inputStream == null){
            return false;
        }
        byte[] data = new byte[10240];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            String fullFileName = imgFilePath + "/" + fileName;
            fileOutputStream = new FileOutputStream(fullFileName);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            result.put("success", 1);
            //result.put("fileUrl", request.getContextPath() + "/upload/" + fileName);
            //result.put("fileUrl_s", request.getContextPath() + "/upload/" + fileName);
            result.put("fileUrl", contextPath + "/upload/" + fileName);
            result.put("fileUrl_s", contextPath + "/upload/" + fileName);
            //String tmpValue = inputStream2String(inputStream);
            String tmpValue = FileHelper.loadTxtFile(fullFileName);
            if (tmpValue.indexOf("access_token is invalid") > 0){
                logger.info("Failed to obtain WeChat image:" + tmpValue);
                if (tryCount > 3){
                    return false;
                }
                tryCount++;
                return downloadMedia(tryCount, imgFilePath, contextPath, media_id, result);
            }
            return true;
        } catch (IOException e) {
            result.put("success", 0);
            result.put("msg", e.toString());
            logger.error(e.toString(), e);
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.toString(), e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.toString(), e);
                }
            }
        }
    }

    public WeixinUser loginByWeixinCallBack_base(CallResult<?> opResult, String userToken, String code, String state) throws Exception {
        //Authorization of basic information on WeChat
        if (StringUtilsEx.isNotEmpty(code) && StringUtilsEx.sameText(state, WEIXIN_AUTH_STATE_BASE)){
            logger.info("========================(2)WeChat authorization base return===========================\n");
            WeixinUser weixinUser = getWeixinUserByBaseCode(opResult, code);
            if (weixinUser != null){
                logger.info(String.format("Previously logged in, there is a user record in the database:%s/%s", weixinUser.getOpenid(), weixinUser.getNickname()));
                logger.info("=====================================================================\n");
                return weixinUser;
            }
            logger.info("===========(2.1)The user has not logged in before, return to the link on the front end to jump to WeChat authorization (UserInfo)=======\n");
        }
        return null;
    }

    public WeixinUser loginByWeixinCallBack_userInfo(CallResult<?> opResult, String userToken, String code, String state) throws Exception {
        //WeChat Full Information Authorization
        if (StringUtilsEx.isNotEmpty(code) && StringUtilsEx.sameText(state, WEIXIN_AUTH_STATE_USER_INFO)){
            logger.info("========================(3)WeChat authorization UserInfo returned===========================\n");
            WeixinUser weixinUser = getWeixinUserByUserInfoCode(opResult, code);
            if (weixinUser != null){
                logger.info(String.format("Successfully obtained the user record:%s/%s", weixinUser.getOpenid(), weixinUser.getNickname()));
                logger.info("=====================================================================\n");
                return weixinUser;
            }
            if (!opResult.isSuccess()){
                logger.info("Failed to obtain user information! The error message is:" + opResult.getMessage());
            }
            logger.info("=====================================================================\n");
        }
        return null;
    }


    public WeixinUser getWeixinUserByBaseCode(CallResult<?> opResult, String code) throws Exception {
        WeixinToken weixinToken = getWeixinTokenByAuthCode(opResult, code);
        if (StringUtilsEx.isNotEmpty(weixinToken.getErrcode())){
            opResult.getAttributes().put("tag", 1);
            opResult.putCode(CallResult.BIZ_ERROR_CODE, String.format("Error calling wechat server: %s/%s", weixinToken.getErrcode(), weixinToken.getErrmsg()));
            return null;

        }
        WeixinUser weixinUser = getWeixinUserByOpenId(opResult, weixinToken.getOpenid());
        if (weixinUser == null){
            opResult.getAttributes().put("tag", 2);
            return null;
        }
        opResult.getAttributes().put("openId", weixinToken.getOpenid());
        return weixinUser;
    }

    public WeixinUser getWeixinUserByUserInfoCode(CallResult<?> opResult, String code) throws Exception {
        WeixinToken weixinToken = getWeixinTokenByAuthCode(opResult, code);
        if (StringUtilsEx.isNotEmpty(weixinToken.getErrcode())){
            opResult.getAttributes().put("tag", 1);
            opResult.putCode(CallResult.BIZ_ERROR_CODE, String.format("Error calling wechat server: %s/%s", weixinToken.getErrcode(), weixinToken.getErrmsg()));
            return null;

        }
        WeixinUser weixinUser = getWeixinUserByOpenId(opResult, weixinToken.getOpenid());
        if (weixinUser == null){
            weixinUser = new WeixinUser();
            //WeiXinUtils.weixinUser2session(opResult, request, response, weixinUser, true);
            opResult.getAttributes().put("tag", 9);
        }
        updateWeixinUserByAccessToken(opResult, weixinUser, weixinToken.getAccess_token(), weixinToken.getOpenid());
        saveWeixinUser(opResult, weixinUser);
        weixinUser = getWeixinUserByOpenId(opResult, weixinToken.getOpenid());
        opResult.getAttributes().put("openId", weixinToken.getOpenid());
        return weixinUser;
    }

    public WeixinToken getWeixinTokenByAuthCode(CallResult<?> opResult, String code) {
        WeixinToken obj = getDataCache().getObj(code, WeixinToken.class);
        if (obj != null) {
            return obj;
        }
        /*
            {
               "access_token":"ACCESS_TOKEN",
               "expires_in":7200,
               "refresh_token":"REFRESH_TOKEN",
               "openid":"OPENID",
               "scope":"SCOPE",
               "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
            }

            {"errcode":40029,"errmsg":"invalid code"}

        */

        WeixinToken token = new WeixinToken();
        String weixinAccessTockenUrl = WeixinRestUrls.getWeixinAccessTockenUrl(weixinTokenHandler().getWeixinAppId(), weixinTokenHandler().getWeixinSecret(), code);
        //String resultJson = HttpClientUtils.doGet(new HttpClient(), weixinUrl.toString(), null);

        HttpClientResult result = null;
        try {
            result = HttpClientUtils.doGet(new HttpConfig(), weixinAccessTockenUrl);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            opResult.error(e.toString());
            return null;
        }
        if (result.getCode() / 100 != 2){
            opResult.putCode(result.getCode(), result.getContent());
            return null;
        }

        String resultJson = result.getContent();
        //logger.info(resultJson);
        Map objMap = JSON.parseObject(resultJson, Map.class);
        if (objMap != null){
            if(objMap.containsKey("errcode")){
                token.setErrcode(ConvertUtils.cStr(objMap.get("errcode")));
                token.setErrmsg(ConvertUtils.cStr(objMap.get("errmsg")));
            }else{
                token.setAccess_token(ConvertUtils.cStr(objMap.get("access_token")));
                token.setExpires_in(ConvertUtils.cStr(objMap.get("expires_in")));
                token.setRefresh_token(ConvertUtils.cStr(objMap.get("refresh_token")));
                token.setOpenid(ConvertUtils.cStr(objMap.get("openid")));
                token.setScope(ConvertUtils.cStr(objMap.get("scope")));
                token.setUnionid(ConvertUtils.cStr(objMap.get("unionid")));
            }
        }
        getDataCache().addCache(code, token, 5);
        return token;
    }

    public void updateWeixinUserByAccessToken(CallResult<?> opResult, WeixinUser weixinUser, String accessToken, String openId) {
        /*
        {
           "openid":" OPENID",
           " nickname": NICKNAME,
           "sex":"1",
           "province":"PROVINCE"
           "city":"CITY",
           "country":"COUNTRY",
            "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
            "privilege":[
            "PRIVILEGE1"
            "PRIVILEGE2"
            ],
            "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
        }

        {"errcode":40003,"errmsg":" invalid openid "}

        */
        String weixinUrl = WeixinRestUrls.getWeixinUserInfoUrl(accessToken, openId);
        //String resultJson = HttpClientUtils.doGet(new HttpClient(), weixinUrl, null);

        HttpClientResult result = null;
        try {
            result = HttpClientUtils.doGet(new HttpConfig(), weixinUrl);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            opResult.error(e.toString());
            return;
        }
        if (result.getCode() / 100 != 2){
            opResult.putCode(result.getCode(), result.getContent());
            return;
        }
        String resultJson = result.getContent();
        logger.info("======Obtain WeChat user information======\n{}\n=================", resultJson);
        Map objMap = JSON.parseObject(resultJson, Map.class);
        updateWeixinUserByMap(weixinUser, objMap);

    }

    public void updateWeixinUserByMap(WeixinUser weixinUser, Map objMap){
        if (objMap != null){
            weixinUser.setCity(ConvertUtils.cStr(objMap.get("city")));
            weixinUser.setCountry(ConvertUtils.cStr(objMap.get("country")));
            weixinUser.setHeadimgurl(ConvertUtils.cStr(objMap.get("headimgurl")));
            weixinUser.setNickname(ConvertUtils.cStr(objMap.get("nickname")));
            weixinUser.setOpenid(ConvertUtils.cStr(objMap.get("openid")));
            weixinUser.setPrivilege(ConvertUtils.cStr(objMap.get("orivilege")));
            weixinUser.setProvince(ConvertUtils.cStr(objMap.get("province")));
            weixinUser.setSex(ConvertUtils.cStr(objMap.get("sex")));
            weixinUser.setUnionid(ConvertUtils.cStr(objMap.get("unionid")));
            weixinUser.setErrcode(ConvertUtils.cStr(objMap.get("errcode")));
            weixinUser.setErrmsg(ConvertUtils.cStr(objMap.get("errmsg")));
        }
    }

}
