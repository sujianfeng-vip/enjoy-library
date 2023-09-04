package vip.sujianfeng.utils.comm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author SuJianFeng
 * @Date 2019/7/15 10:09
 **/
public class JsonPathUtils {

    /**
     * 根据完整路径key，获取多层json结构的内部数据
     * 例如
     * 输入： json:  {field1: { field11: 123 }}
     *       pathKey: field1.field11
     * 返回： 123
     *
     * @param json
     * @param pathKey
     * @return
     */
    public static Object getValue(JSONObject json, String pathKey){
        if (json == null){
            return null;
        }
        if (!pathKey.contains(".")){
            return json.get(pathKey);
        }
        String left = StringUtilsEx.leftStr(pathKey, ".");
        String right = StringUtilsEx.rightStrEx(pathKey, ".");
        return getValue(json.getJSONObject(left), right);
    }

    public static Object getValue(String json, String pathKey){
        if (StringUtilsEx.isEmpty(json)){
            return null;
        }
        return getValue(JSON.parseObject(json), pathKey);
    }

    public static void main(String[] args){
        String json = "{\"field1\": { \"field2\": {\"field3\": 33}}}";
        System.out.println("field1: " + getValue(json, "field1"));
        System.out.println("field1.field2: " + getValue(json, "field1.field2"));
        System.out.println("field1.field2.field3: " + getValue(json, "field1.field2.field3"));
    }
}
