package vip.sujianfeng.utils.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2019/9/11 13:46
 **/
public class MapUtilsEx {

    public static <T> Map<String, T> mapOf(Object... items){
        Map result = new HashMap();
        for (int i = 0; i < items.length; i += 2) {
            result.put(items[i], items[i + 1]);
        }
        return result;
    }
}
