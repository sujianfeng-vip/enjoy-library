package vip.sujianfeng.utils.comm;

/**
 * author SuJianFeng
 * createTime  2023/2/15
 * Description
 **/
public class IdUtils {

    public static String merge(String... ids) {
        if (ids.length == 0) {
            return "";
        }
        if (ids.length == 1) {
            return ids[0];
        }
        int avg = 50 / ids.length - 1;
        StringBuilderEx s = new StringBuilderEx();
        for (String id : ids) {
            if (s.length() > 0) {
                s.append("-");
            }
            int endIndex = Math.min(id.length(), avg);
            s.append(id.substring(0, endIndex));
        }
        return s.toString();
    }
}
