package vip.sujianfeng.engine.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScriptObjectConvertUtils {

    /**
     * 将ScriptObjectMirror对象转化为JSON对象
     * 主要是对数组进行特殊处理
     * @param object
     * @return
     */
    public static Object convert2json(Object object){
        Object o = object;
        if (object instanceof ScriptObjectMirror){
            o = JSON.toJSON(object);
        }
        if (o instanceof JSONObject){
            JSONArray array = tryConvertArray((JSONObject) o);
            if (array != null){
                return convertJsonArray(array);
            }
            return convertJsonObject((JSONObject) o);
        }
        if (o instanceof JSONArray){
            return convertJsonArray((JSONArray) o);
        }
        return o;
    }

    /**
     * 尝试看能否转化为数组
     * @param object
     * @return
     */
    private static JSONArray tryConvertArray(JSONObject object){
        if (object == null){
            return null;
        }
        Set<String> keys = object.keySet();
        if (keys.size() == 0){
            return null;
        }
        List<String> collect = keys.stream().sorted(Comparator.comparingInt(ConvertUtils::cInt)).collect(Collectors.toList());
        JSONArray result = new JSONArray();
        for (int i = 0; i < collect.size(); i++) {
            String key = collect.get(i);
            if (!StringUtilsEx.isInteger(key) || ConvertUtils.cInt(key) != i){
                return null;
            }
            result.add(object.get(key));
        }
        return result;
    }

    private static JSONObject convertJsonObject(JSONObject object){
        JSONObject result = new JSONObject();
        Set<String> keys = object.keySet();
        for (String key : keys) {
            Object o = object.get(key);
            o = convert2json(o);
            result.put(key, o);
        }
        return result;
    }

    private static JSONArray convertJsonArray(JSONArray array){
        JSONArray result = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            Object o = array.get(i);
            result.add(convert2json(o));
        }
        return result;
    }
}
