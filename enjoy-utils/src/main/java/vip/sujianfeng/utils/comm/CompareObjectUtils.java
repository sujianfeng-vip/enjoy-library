package vip.sujianfeng.utils.comm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Comparison tool class
 */
public class CompareObjectUtils {

    public static boolean isSame(Object obj1, Object obj2, List<String> fileds) {
        JSONObject json1 = JSON.parseObject(JSON.toJSONString(obj1));
        JSONObject json2 = JSON.parseObject(JSON.toJSONString(obj2));
        for (String field : fileds) {
            if (!isSame(json1.getString(field), json2.getString(field))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSame(Object obj1, Object obj2, List<String> filed1s, List<String> filed2s) {
        JSONObject json1 = JSON.parseObject(JSON.toJSONString(obj1));
        JSONObject json2 = JSON.parseObject(JSON.toJSONString(obj2));
        for (int i = 0; i < filed1s.size(); i++) {
            String f1 = filed1s.get(i);
            if (filed2s.size() <= i) {
                return true;
            }
            String f2 = filed2s.get(i);
            if (!isSame(json1.getString(f1), json2.getString(f2))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSame(String str1, String str2) {
        str1 = ConvertUtils.cStr(str1);
        str2 = ConvertUtils.cStr(str2);
        if ("null".equalsIgnoreCase(str1)) {
            str1 = "";
        }
        if ("null".equalsIgnoreCase(str2)) {
            str2 = "";
        }
        if (StringUtilsEx.sameText(str1, str2)) {
            return true;
        }
        if (JsonUtilsEx.isJsonStr(str1) && JsonUtilsEx.isJsonStr(str2)) {
            return isSame(JSON.parseObject(str1), JSON.parseObject(str2));
        }
        if (JsonUtilsEx.isJsonArrStr(str1) && JsonUtilsEx.isJsonArrStr(str2)) {
            return isSame(JSON.parseArray(str1), JSON.parseArray(str2));
        }
        return false;
    }

    public static boolean isSame(Object o1, Object o2) {
        if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
            if (!isSame((JSONObject) o1, (JSONObject) o2)) {
                return false;
            }
        }
        if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
            if (!isSame((JSONArray) o1, (JSONArray) o2)) {
                return false;
            }
        }
        if (!isSame(ConvertUtils.cStr(o1), ConvertUtils.cStr(o2))) {
            return false;
        }
        return true;
    }

    public static boolean isSame(JSONObject json1, JSONObject json2) {
        if (json1.size() != json2.size()) {
            return false;
        }
        for (String key : json1.keySet()) {
            if (!isSame(json1.get(key), json2.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSame(JSONArray arr1, JSONArray arr2) {
        if (arr1.size() != arr2.size()) {
            return false;
        }
        for (int i = 0; i < arr1.size(); i++) {
            if (!isSame(arr1.get(i), arr2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
