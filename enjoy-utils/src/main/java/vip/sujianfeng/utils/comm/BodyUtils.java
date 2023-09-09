package vip.sujianfeng.utils.comm;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;

/**
 * author zhangmaohua
 * createTime  2019/7/22 14:13
 */
public class BodyUtils {

    private static Logger logger = LoggerFactory.getLogger(BodyUtils.class);

    public static List<String> bodyToArr(String key, Object obj, List<String> toArr, boolean encode) {
        if (null == obj) {
            return toArr;
        }
        if (isPrimitiveDataTypes(obj)) {
            if (!StringUtilsEx.isEmpty(obj)){
                String value = String.valueOf(obj);
                if (encode){
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.toString(), e);
                    }
                }
                toArr.add(key + "=" + value);
            }
            return toArr;
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.isEmpty()) {
                return toArr;
            }
            Object[] objects = collection.toArray();
            if (isPrimitiveDataTypes(objects[0])) {
                if (!key.isEmpty()) {
                    toArr.add(key + "=" + join("", collection));
                } else {
                    toArr.add(join("", collection));
                }
                return toArr;
            }
            for (int i = 0; i < objects.length; i++) {
                Object o = objects[i];
                if (!key.isEmpty()) {
                    bodyToArr(key + "." + i, o, toArr, encode);
                } else {
                    bodyToArr(String.valueOf(i), o, toArr, encode);
                }
            }
            return toArr;
        }
        Map map = JSON.parseObject(JSON.toJSONString(obj), Map.class);
        if (map.isEmpty()) {
            return toArr;
        }
        for (Object fieldName : map.keySet()) {
            Object o = map.get(fieldName);
            if (!key.isEmpty()) {
                bodyToArr(key + "." + fieldName.toString(), o, toArr, encode);
            } else {
                bodyToArr(String.valueOf(fieldName), o, toArr, encode);
            }
        }
        return toArr;
    }

    private static String join(CharSequence delimiter, Iterable<Object> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object obj: elements) {
            joiner.add(String.valueOf(obj));
        }
        return joiner.toString();
    }

    public static String arrToStr(List<String> arr) {
        if (null == arr || arr.isEmpty()) {
            return "";
        }
        Collections.sort(arr);
        return String.join("&", arr);
    }

    public static boolean isPrimitiveDataTypes(Object obj) {
        if (obj.getClass().isPrimitive()){
            return true;
        }
        //String individualization
        if (obj instanceof String) {
            return true;
        }
        if (obj instanceof Byte) {
            return true;
        }
        if (obj instanceof Short) {
            return true;
        }
        if (obj instanceof Integer) {
            return true;
        }
        if (obj instanceof Long) {
            return true;
        }
        if (obj instanceof Character) {
            return true;
        }
        if (obj instanceof Float) {
            return true;
        }
        if (obj instanceof Double) {
            return true;
        }
        if (obj instanceof Boolean) {
            return true;
        }
        if (obj instanceof BigInteger) {
            return true;
        }
        if (obj instanceof BigDecimal) {
            return true;
        }
        return false;
    }

    public static boolean isPrimitiveDataTypes(Type type) {
        String typeName = type.getTypeName();
        if ("java.lang.String".equals(typeName)){
            return true;
        }
        if ("java.lang.Byte".equals(typeName)){
            return true;
        }
        if ("java.lang.Short".equals(typeName)){
            return true;
        }
        if ("java.lang.Integer".equals(typeName)){
            return true;
        }
        if ("java.lang.Long".equals(typeName)){
            return true;
        }
        if ("java.lang.Character".equals(typeName)){
            return true;
        }
        if ("java.lang.Float".equals(typeName)){
            return true;
        }
        if ("java.lang.Double".equals(typeName)){
            return true;
        }
        if ("java.lang.Boolean".equals(typeName)){
            return true;
        }
        return false;
    }
}
