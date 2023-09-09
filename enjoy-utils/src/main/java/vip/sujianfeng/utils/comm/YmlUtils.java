package vip.sujianfeng.utils.comm;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2020/2/27 9:52
 * Yaml File Tool Class
 **/
public class YmlUtils {

    public static Map<String, Object> getYml(String ymlFile){
        return ConfigUtils.getYml(ymlFile);
    }

    public static String getYmlValueString(Map<String, Object> ymlMap, String key){
        return ConvertUtils.cStr(getYmlValue(ymlMap, key));
    }
    public static Object getYmlValue(Map<String, Object> ymlMap, String key){
        if (ymlMap.containsKey(key) || !key.contains(".")){
            return ymlMap.get(key);
        }
        String keyLeft = StringUtilsEx.leftStr(key, ".");
        Object o = ymlMap.get(keyLeft);
        if (!(o instanceof Map)){
            return null;
        }
        String keyRight = StringUtilsEx.rightStrEx(key, ".");
        return getYmlValue((Map<String, Object>) ymlMap.get(keyLeft), keyRight);
    }

    public static <T> T loadYml(Class<T> t, String ymlFile) {
        Map<String, Object> yml = getYml(ymlFile);
        if (yml != null) {
            String s = JSON.toJSONString(yml);
            return JSON.parseObject(s, t);
        }
        return null;
    }
}
