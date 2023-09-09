package vip.sujianfeng.token;

import com.alibaba.fastjson.JSON;
import vip.sujianfeng.utils.comm.Base64UtilsEx;

/**
 * author SuJianFeng
 * createTime  2022/6/28
 * Description
 **/
public class JwtToken {

    public static String newTokenByExpireDays(JwtTokenData data, String seed, int expireDays) throws Exception {
        return newToken(data, seed, System.currentTimeMillis() / 1000 + expireDays * 24 * 3600L);
    }

    public static String newToken(JwtTokenData data, String seed, long expireTime) throws Exception {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setExpireTime(expireTime);
        jwtToken.setData(JwtTokenData.encrypt(data, seed, expireTime));
        return Base64UtilsEx.encodeStr(JSON.toJSONString(jwtToken));
    }

    public static JwtTokenData parseToken(String token, String seed) throws Exception {
        String jwtTokenJson = Base64UtilsEx.decodeStr(token);
        JwtToken jwtToken = JSON.parseObject(jwtTokenJson, JwtToken.class);
        if (System.currentTimeMillis() > jwtToken.expireTime * 1000L) {
            throw new Exception("Token expired!");
        }
        return JwtTokenData.decrypt(jwtToken.data, seed, jwtToken.expireTime);
    }

    public static String updateTokenExpireDays(String token, String seed, int expireDays) throws Exception {
        return updateTokenExpireTime(token, seed, System.currentTimeMillis() / 1000 + expireDays * 24 * 3600L);
    }

    public static String updateTokenExpireTime(String token, String seed, long expireTime) throws Exception {
        JwtTokenData jwtTokenData = parseToken(token, seed);
        return newToken(jwtTokenData, seed, expireTime);
    }

    private long expireTime;
    private String data;

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
