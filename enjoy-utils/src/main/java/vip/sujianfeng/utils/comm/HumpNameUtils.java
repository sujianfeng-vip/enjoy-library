package vip.sujianfeng.utils.comm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 苏建锋
 * @create 2020-03-07 11:23
 * 驼峰与横线互相转换工具类
 *
 */
public class HumpNameUtils {

    private static Pattern MID_LINE_PATTERN = Pattern.compile("-(\\w)");
    private static Pattern UNDER_LINE_PATTERN = Pattern.compile("_(\\w)");
    private static Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /** 下划线转驼峰 */
    public static String underLineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = UNDER_LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String midLineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = MID_LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 驼峰转下划线(简单写法，效率低于{@link #humpToUnderLine2(String)}) */
    public static String humpToUnderLine(String str) {
        String result = str.replaceAll("[A-Z]", "_$0").toLowerCase();
        return result.startsWith("_") ? result.substring(1) : result;
    }
    public static String humpToMidLine(String str) {
        String result = str.replaceAll("[A-Z]", "-$0").toLowerCase();
        return result.startsWith("-") ? result.substring(1) : result;
    }

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToUnderLine2(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String result = sb.toString();
        return result.startsWith("_") ? result.substring(1) : result;
    }

    public static String humpToMidLine2(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String result = sb.toString();
        return result.startsWith("-") ? result.substring(1) : result;
    }
}
