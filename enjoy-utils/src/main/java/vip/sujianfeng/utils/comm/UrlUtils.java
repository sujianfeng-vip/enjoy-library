package vip.sujianfeng.utils.comm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujianfeng on 14-10-10.
 */
public class UrlUtils {

    public static String decodeId(String url) throws UnsupportedEncodingException {
        String result = ConvertUtils.cStr(url);
        result = result.replace("~", "%");
        result = result.replace("_", "/");
        return java.net.URLDecoder.decode(url, "UTF-8");
    }

    public static Map<String, Object> urlParams2map(String urlParams){
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> items = StringUtilsEx.splitString(urlParams, "&");
        for (String item : items){
            List<String> keyAndValue = StringUtilsEx.splitString(item, "=");
            if (keyAndValue.size() > 0){
                if (keyAndValue.size() > 1){
                    result.put(keyAndValue.get(0), keyAndValue.get(1));
                }else{
                    result.put(keyAndValue.get(0), null);
                }
            }
        }
        return result;
    }
}
