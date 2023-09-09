package vip.sujianfeng.token;

import com.alibaba.fastjson.JSON;

/**
 * author SuJianFeng
 * createTime  2022/6/28
 * Description
 **/
public class JwtTokenTest {

    public static void main(String[] args) throws Exception {
        String seed = "hougu@2022";
        JwtTokenData data = new JwtTokenData();
        data.setUserId("id-1");
        data.setLoginCorpId("corp-1");
        data.setLoginAppId("app-1");
        data.setUserType(3);
        String token = JwtToken.newTokenByExpireDays(data, seed, 7);
        JwtTokenData jwtTokenData = JwtToken.parseToken(token, seed);
        System.out.println(JSON.toJSONString(jwtTokenData));
        String newToken = JwtToken.updateTokenExpireTime(token, seed, jwtTokenData.getExpireTime() + 7 * 24 * 3600);
        jwtTokenData = JwtToken.parseToken(newToken, seed);
        System.out.println(JSON.toJSONString(jwtTokenData));
    }
}
