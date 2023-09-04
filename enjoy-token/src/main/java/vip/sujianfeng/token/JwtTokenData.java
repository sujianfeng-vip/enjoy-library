package vip.sujianfeng.token;

import vip.sujianfeng.utils.comm.AesUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.util.List;

/**
 * @Author SuJianFeng
 * @Date 2022/6/28
 * @Description
 **/
public class JwtTokenData {

    /**
     * 加密token数据
     * @param data
     * @param seed
     * @param expireTime
     * @return
     * @throws Exception
     */
    public static String encrypt(JwtTokenData data, String seed, long expireTime) throws Exception {
        return AesUtils.AESEncode(
                String.format("%s%s", seed, expireTime),
                String.format("%s,%s,%s,%s,%s,%s", data.userId, data.loginCorpId, data.loginAppId, data.userType,data.openId ,data.telephone)
        );
    }

    /**
     * 解密token数据
     * @param encryptStr
     * @param seed
     * @param expireTime
     * @return
     * @throws Exception
     */
    public static JwtTokenData decrypt(String encryptStr, String seed, long expireTime) throws Exception {
        String content = AesUtils.AESDecode(
                String.format("%s%s", seed, expireTime),
                encryptStr);
        List<String> items = StringUtilsEx.splitString(content, ",");
        JwtTokenData data = new JwtTokenData();
        data.expireTime = expireTime;
        if (items.size() > 0) data.setUserId(items.get(0));
        if (items.size() > 1) data.setLoginCorpId(items.get(1));
        if (items.size() > 2) data.setLoginAppId(items.get(2));
        if (items.size() > 3) data.setUserType(ConvertUtils.cInt(items.get(3)));
        if (items.size() > 4) data.setOpenId(ConvertUtils.cStr(items.get(4)));
        if (items.size() > 5) data.setTelephone(ConvertUtils.cStr(items.get(5)));
        return data;
    }

    private String userId;
    private String openId;
    private String telephone;
    private String loginCorpId;
    private String loginAppId;
    private int userType;
    private long expireTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginCorpId() {
        return loginCorpId;
    }

    public void setLoginCorpId(String loginCorpId) {
        this.loginCorpId = loginCorpId;
    }

    public String getLoginAppId() {
        return loginAppId;
    }

    public void setLoginAppId(String loginAppId) {
        this.loginAppId = loginAppId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
