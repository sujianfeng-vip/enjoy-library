package vip.sujianfeng.utils.comm;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtilsEx {

	public static String A2Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String A2Z(int index) {
		return String.valueOf(A2Z.toCharArray()[index]);
	}
	
	public static int abc2int(String letter){
		letter = letter.toUpperCase();
		if (letter.length() == 1){
			char c = letter.toCharArray()[0];
			return c - 'A'; 
		}else{
			return 0;
		}			
	}

	public static String replaceByMap(String text, String leftTag, String rightTag, Map<String, Object> param) {
		if (text.contains(leftTag) && text.contains(rightTag)){
			String left = StringUtilsEx.leftStr(text, leftTag);
			String right = StringUtilsEx.rightStrEx(text, leftTag);
			String key = StringUtilsEx.leftStr(right, rightTag);
			right = StringUtilsEx.rightStrEx(text, rightTag);
			String value = ConvertUtils.cStr(param.get(key));
			text = replaceByMap(String.format("%s%s%s", left, value, right), leftTag, rightTag, param);
		}
		return text;
	}

	public static int containCount(String text, char c) {
		int result = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == c)
				result++;
		}
		return result;
	}

	public static boolean isNumeric(Object str) {
		boolean result = false;
		if (str != null) {
			result = true;
			try {
				new BigDecimal(str.toString());
			} catch (Exception e) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	public static boolean isInteger(Object o) {
		try {
			Integer.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String leftStr(String text, int len) {
		return text.substring(0, len);
	}

	public static String rightStr(String text, int len) {
		String result = "";
		for (int i = text.length() - 1; i <= 0; i--) {
			if (i + len >= text.length())
				result = text.charAt(i) + result;
			else
				break;
		}
		return result;
	}

	public static String leftStr(String text, String splitStr) {
		int iPos = text.indexOf(splitStr);
		if (iPos >= 0)
			return text.substring(0, iPos);
		else
			return text;

	}

	/**
	 * 取分隔符靠左的文本
	 * 分隔符在最右边
	 * @param text
	 * @param splitStr
	 * @return
	 */
	public static String leftStrEx(String text, String splitStr) {
		int iPos = text.lastIndexOf(splitStr);
		if (iPos >= 0)
			return text.substring(0, iPos);
		else
			return text;

	}

	public static String rightStr(String text, String splitStr) {
		int iPos = text.lastIndexOf(splitStr);
		if (iPos >= 0) {
			return text.substring(iPos + splitStr.length());
		} else {
			return text;
		}
	}

	/**
	 * 取分隔符靠右的文本
	 * 分隔符在最左边
	 * @param text
	 * @param splitStr
	 * @return
	 */
	public static String rightStrEx(String text, String splitStr) {
		int iPos = text.indexOf(splitStr);
		if (iPos >= 0) {
			return text.substring(iPos + splitStr.length());
		} else {
			return text;
		}
	}

	public static boolean sameText(String text1, String text2) {
		if (text1 == null)
			text1 = "";
		if (text2 == null)
			text2 = "";
		return text1.equalsIgnoreCase(text2);
	}
	
	public static boolean sameText(Object obj1, Object obj2) {
		String text1 = ConvertUtils.cStr(obj1);
		String text2 = ConvertUtils.cStr(obj2);
		return sameText(text1, text2);
	}

	public static boolean isEmpty(String text) {
		return (text == null || "".equals(text.trim()));
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		return isEmpty(String.valueOf(obj));
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
	public static String mergeStrings(String text1, String text2, String div) {
		String result = text1;
		if (!isEmpty(text2)) {
			if (!isEmpty(result))
				result += div + text2;
			else
				result = text2;
		}
		return result;
	}

	public static String copyString(String item, int copyCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < copyCount; i++) {
			sb.append(item);
		}
		return sb.toString();
	}

	// 截取指定字段对包括内的字符串
	public static String subStringByDiv(String text, String beginDiv,
			String endDiv) {
		int begin = text.indexOf(beginDiv) + beginDiv.length();
		int end = text.indexOf(endDiv);
		String result = "";
		if (begin > -1 && end > -1 && end > begin)
			result = text.substring(begin, end);
		return result;
	}

	/**
	 * 首字母大写
	 * 
	 * @param item
	 * @return
	 */
	public static String firstWordUpCase(String item) {
		if (isEmpty(item))
			return "";
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(item.toUpperCase().toCharArray()[0]);
			sb.append(item.substring(1));
			return sb.toString();
		}
	}

	/**
	 * 首字母小写
	 * 
	 * @param item
	 * @return
	 */
	public static String firstWordLowerCase(String item) {
		if (isEmpty(item))
			return "";
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(item.toLowerCase().toCharArray()[0]);
			sb.append(item.substring(1));
			return sb.toString();
		}
	}

	public static ArrayList<String> splitString(String str, String div) {
		ArrayList<String> result = new ArrayList<String>();
		if (StringUtilsEx.isNotEmpty(str)){
			String[] strs = str.split(div);
			for (int i = 0; i < strs.length; i++) {
				result.add(strs[i]);
			}
		}
		return result;
	}

	/**
	 * 次函数正确性未得到验证
	 * 
	 * @param origin
	 *            原始字符串
	 * @param start
	 *            起始位置
	 * @param len
	 *            截取长度(一个汉字长度按2算的) ,长度超过取到原始字符串末
	 * @return 返回的字符串
	 */
	public static String subCnString(String origin, int start, int len) {
		class SubCnString {

			/**
			 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
			 * 
			 * 
			 * @param origin
			 *            原始字符串
			 * @param start
			 *            起始位置
			 * @param len
			 *            截取长度(一个汉字长度按2算的) ,长度超过取到原始字符串末
			 * @return 返回的字符串
			 */
			public String substring(String origin, int start, int len) {
				if (origin == null || origin.equals("") || len < 1)
					return "";
				byte[] strByte = new byte[len];
				if (start + len > length(origin)) {
					len = length(origin) - start;
				}
				try {
					System.arraycopy(origin.getBytes("UTF-8"), start, strByte, 0,
							len);
					int count = 0;
					for (int i = 0; i < len; i++) {
						int value = (int) strByte[i];
						if (value < 0) {
							count++;
						}
					}
					if (count % 2 != 0) {
						len = (len == 1) ? ++len : --len;
					}
					return new String(strByte, 0, len, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return (new SubCnString()).substring(origin, start, len);
	}
	
	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 返回true,Ascill字符
	 */

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}	
	
	public static List<String> arrToList(String[] arr){
		List<String> paramlist = new ArrayList<String>();
		for (String string : arr) {
			paramlist.add(string);
		}
		return paramlist;
	}
	
	public static String encodeString(String s, String charset) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes(charset);
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	public static boolean isLong(Object o) {
		try {
			Long.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String inc(String billNo){
		if (billNo.length() > 0){
			char c = billNo.toCharArray()[billNo.length() - 1];		
			String left = leftStr(billNo, billNo.length() - 1);
			if ((c >= '0' && c <= '8')||(c >= 'A' && c <= 'Y')||(c >= 'a' && c <= 'y')){
				c++;
				return left + c;
			}else if (c == '9'){
				left = inc(left);
				return left + '0';
			}else if (c == 'Z'){
				left = inc(left);
				return left + 'A';
			}else if (c == 'z'){
				left = inc(left);
				return left + 'a';
			}	
		}		
		return billNo;
	}
	
	public static String subStr(String str, int start, int length) {
		if(str == null)
			return "";
		int endIndex = start + length;
		if(endIndex > str.length())
			endIndex = str.length();
		return str.substring(start, endIndex);
	}

	public static String arr2text(String[] arrs, String div){
		StringBuilder result = new StringBuilder();
		for (String item : arrs) {
			if (result.length() > 0){
				result.append(div);
			}
			result.append(item);
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		String aa = "我是字符串";
		System.out.println(subStr(aa, 0, 6));
		String fileName ="abc.text";
		System.out.println(getFileSuffix(fileName));
		System.out.println(genFileName(getFileSuffix(fileName)));
	}

	public static boolean isCN(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length > 1) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	public static String getRandCode(){
		Random random = new Random();
		return String.valueOf(random.nextInt(89999999)+10000000);
	}

	public static String genFileName(String suffix){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHmmssS");
		return sdf.format(new Date()) + suffix;
	}

	public static String getFileSuffix(String fileName){
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long month = 31 * day;// 月
	private final static long year = 12 * month;// 年
	/**
	 * 返回文字描述的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(Date date) {
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}

	/**
	 * 如果value不为空，返回notEmptyResult，反正返回emptyResult
	 * @param value
	 * @param notEmptyResult
	 * @param emptyResult
	 * @return
	 */
	public static String iifNotNull(String value, String notEmptyResult, String emptyResult) {
		return StringUtilsEx.isNotEmpty(value) ? notEmptyResult : emptyResult;
	}

	/**
	 * 取出空白字符（空格、制表符、换页符等空白字符）
	 * @param text
	 * @return
	 */
	public static String removeEmpty(String text) {
		return ConvertUtils.cStr(text).replace(" ", "");
	}

	public static String f2(int num) {
		if (num < 10){
			return String.format("0%s", num);
		}
		return String.format("%s", num);
	}

	public static String f3(int num) {
		if (num < 10){
			return String.format("00%s", num);
		}
		if (num < 100){
			return String.format("0%s", num);
		}
		return String.format("%s", num);
	}
}
