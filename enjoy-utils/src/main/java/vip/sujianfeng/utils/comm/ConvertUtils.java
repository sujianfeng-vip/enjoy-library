package vip.sujianfeng.utils.comm;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtils {
	public static int cInt(Object value) {
		if (value instanceof Double){
			return cFloat(value).intValue();
		}
		if (value instanceof Boolean){
			return (Boolean)value ? 1 : 0;
		}
		if (value instanceof BigDecimal){
			return ((BigDecimal)value).intValue();
		}
		if (StringUtilsEx.isInteger(value))
			return Integer.valueOf(value.toString());
		else
			return 0;
	}

	public static BigDecimal cFloat(Object value) {
		String text = cStr(value);
		text = text.replace(",", "");
		if (StringUtilsEx.isNumeric(text)){
			return new BigDecimal(text);
		}else{
			return new BigDecimal(0.0);
		}
	}

	public static boolean oneOfInt(int value, int... ints) {
		boolean result = false;
		for (int i = 0; i < ints.length; i++) {
			if (value == ints[i]) {
				result = true;
				break;
			}
		}
		return result;
	}


	public static boolean oneOfString(String[] items, String item) {
		return indexOf(items, item) != -1;
	}

	public static int indexOf(String[] items, String item) {
		int result = -1;
		for (int i = 0; i < items.length; i++) {
			if (StringUtilsEx.sameText(item, items[i])) {
				result = i;
				break;
			}
		}
		return result;
	}

	public static String cStr(Object value) {
		if (value == null){
			return "";
		}
		String result = String.valueOf(value);
		if ("null".equals(result)){
			return "";
		}
		return result;
	}
	
	public static boolean cBool(Object value) {
		if (StringUtilsEx.isEmpty(value))
			return false;
		if (StringUtilsEx.sameText("1", value))
			return true;
		if (StringUtilsEx.sameText("0", value))
			return false;
		if (StringUtilsEx.sameText("on", value))
			return true;
		if (StringUtilsEx.sameText("off", value))
			return false;
		return Boolean.valueOf(String.valueOf(value));
	}

	public static String quoStr(Object value) {
		if (value == null)
			return "''";
		else
			return String.format("'%s'", value.toString());
	}

	public static String encloseStr(String src, String enclose) {
		return enclose + src + enclose;
	}
	
	public static StringBuilder list2StringBuilder(List<String> list) {
		StringBuilder result = new StringBuilder();
		for (String item : list) {
			result.append(item).append("\n");
		}
		return result;
	}
	
	public static Long cLong(Object value) { 
		if (StringUtilsEx.isLong(value))
			return Long.valueOf(value.toString());
		else
			return (long) 0; 
	}
	
	/**
	 * 将字符串数组转成按指定分隔符串联的字符串，并对每个元素按decorate包含（比如单引号）
	 * @param array
	 * @param decorate
	 * @param separator
	 * @return
	 */
	public static String array2String(String[] array, String decorate, String separator) {
		StringBuilder sb = new StringBuilder();
		for(String ele : array) {
			sb.append(encloseStr(ele, decorate)).append(separator);
		}
		if(sb.indexOf(separator)>-1)
			return sb.substring(0, sb.lastIndexOf(separator));
		else
			return sb.toString();
	}
	
	public static String list2String(List<String> list, String separator) {
		String[] array = list.toArray(new String[list.size()]);
		return array2String(array, "", separator);
	}
	
	public static String list2SqlValueString(List<String> list) {
		String[] array = list.toArray(new String[list.size()]);
		return array2String(array, "'", ",");
	} 
	
	public static boolean oneOfString(String obj, String... objs){
		boolean result = false;
		for (int i = 0; i < objs.length; i++) {
			if (StringUtilsEx.sameText(obj, objs[i])){
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean oneOfString(String obj, List<String> list){
		boolean result = false;
		for (int i = 0; i < list.size(); i++) {
			if (StringUtilsEx.sameText(obj, list.get(i))){
				result = true;
				break;
			}
		}
		return result;
	}


	public static boolean oneOfObj(String obj, Object... objs){
		boolean result = false;
		for (int i = 0; i < objs.length; i++) {
			if (obj == null && objs[i] == null){
				result = true;
				break;
			}
			if (obj.equals(objs[i])){
				result = true;
				break;
			}
		}
		return result;
	}	 
	
	
	public static boolean swap(List<Object> list, int a, int b){
		if (a == b)
			return false;
		Object objA = list.get(a);  
		list.set(a, list.get(b));  
		list.set(b, objA);
		return true;
	}
	
	public static String formatNumber(Object value, int digCount, boolean groupingUsed){
		return formatNumber(cFloat(value), digCount, groupingUsed); 
	}
	
	public static String f2(Object value){
		return formatNumber(cFloat(value), 2, true); 
	}

	public static double cDouble(Object value, int digCount){
		return cDouble(formatNumber(cFloat(value), digCount, true));
	}
	
	public static String formatNumber(BigDecimal value, int digCount, boolean groupingUsed){
		DecimalFormat nf = new DecimalFormat();  
		nf.setGroupingUsed(groupingUsed); //千分位逗号
		nf.setMaximumFractionDigits(digCount); 
		nf.setMinimumFractionDigits(digCount);
		return nf.format(Double.valueOf(value.toString())); 
	}
	 
	public static Map<String, String> urlParams2map(String url){
		Map<String, String> result = new HashMap<String, String>();
		if (StringUtilsEx.isNotEmpty(url)){
			String[] items = url.split("&");
			for (String item : items) {
				String[] arr = item.split("=");
				String key = arr[0];
				String value = "";
				if (arr.length > 1){
					value = arr[1];
				}
				result.put(key, value);
			}
		}
		return result;
	}

	public static double cDouble(Object value) {
		if (value != null && StringUtilsEx.isNumeric(value)){
			return Double.valueOf(value.toString());
		}
		return 0.0;
	}

	public static short cShort(String value) {
		return (short) ConvertUtils.cInt(value);
	}

    /**
     * 如果obj为空，那么用ifNullReturnObj代替
     * @param obj
     * @param ifNullReturnObj
     * @param <T>
     * @return
     */
	public static <T> T ifNull(T obj, T ifNullReturnObj){
		return obj != null ? obj : ifNullReturnObj;
	}

    /**
     * 获取map中对应key的value，如果为null那么用ifNullReturnObj对象代替
     * @param map
     * @param key
     * @param ifNullReturnObj
     * @param <T>
     * @return
     */
	public static <T> T ifNull(Map map, Object key, T ifNullReturnObj){
	    return  (map == null || !map.containsKey(key)) ? ifNullReturnObj : (T) map.get(key);
    }

	public static void main(String[] args) {
        System.out.println(ifNull(1, 2));
        System.out.println(ifNull(null, 2));
    }
}
