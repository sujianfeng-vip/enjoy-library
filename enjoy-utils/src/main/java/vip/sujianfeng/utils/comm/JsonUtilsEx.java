package vip.sujianfeng.utils.comm;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * author SuJianFeng
 * createTime  2019/8/2 15:03
 **/
public class JsonUtilsEx {

    public static boolean isJsonStr(String json){
        try{
            if (StringUtilsEx.isEmpty(json)) {
                return false;
            }
            JSON.parseObject(json);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isJsonArrStr(String json){
        try{
            if (StringUtilsEx.isEmpty(json)) {
                return false;
            }
            JSON.parseArray(json);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static <T> T parse(Class<T> t, Object obj) {
        return JSON.parseObject(JSON.toJSONString(obj), t);
    }

    public static <T> List<T> parseList(Class<T> t, Object obj) {
        return JSON.parseArray(JSON.toJSONString(obj), t);
    }
}
