package vip.sujianfeng.utils.comm;


/**
 * Created by pilot on 2015/3/19.
 */
public class SqlUtils {

    public final static String DATE_FORMAT = "%Y-%m-%d";
    public final static String DATE_TIME_FORMAT = "%Y-%m-%d %T";

    /**
     * 适用于数据库字段为正常日期字段
     * @param fieldName
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String dateWhere(String fieldName, String beginDate, String endDate){
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            if (beginDate.compareTo(endDate) > 0){
                String tmp = beginDate;
                beginDate = endDate;
                endDate = tmp;
            }
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && beginDate.length() >= 8 && beginDate.length() <= 10){
            beginDate += " 00:00:00";
        }
        if (StringUtilsEx.isNotEmpty(endDate) && endDate.length() >= 8 && endDate.length() <= 10){
            endDate += " 23:59:59";
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            return String.format(" And (%s between STR_TO_DATE('%s', '%s') and STR_TO_DATE('%s', '%s'))", fieldName, beginDate, SqlUtils.DATE_TIME_FORMAT, endDate, SqlUtils.DATE_TIME_FORMAT);
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isEmpty(endDate)){
            return String.format(" And %s >= STR_TO_DATE('%s', '%s')", fieldName, beginDate, SqlUtils.DATE_TIME_FORMAT);
        }
        if (StringUtilsEx.isEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            return String.format(" And %s <= STR_TO_DATE('%s', '%s')", fieldName, endDate, SqlUtils.DATE_TIME_FORMAT);
        }
        return "";
    }

    /**
     * 适用于用数据库是字符串字段存日期
     * @param fieldName
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String dateStrWhere(String fieldName, String beginDate, String endDate){
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            if (beginDate.compareTo(endDate) > 0){
                String tmp = beginDate;
                beginDate = endDate;
                endDate = tmp;
            }
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && beginDate.length() == 10){
            beginDate += " 00:00:00";
        }
        if (StringUtilsEx.isNotEmpty(endDate) && endDate.length() == 10){
            endDate += " 23:59:59";
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            return String.format(" And (%s between '%s' and '%s')", fieldName, beginDate, endDate);
        }
        if (StringUtilsEx.isNotEmpty(beginDate) && StringUtilsEx.isEmpty(endDate)){
            return String.format(" And %s >= '%s'", fieldName, beginDate);
        }
        if (StringUtilsEx.isEmpty(beginDate) && StringUtilsEx.isNotEmpty(endDate)){
            return String.format(" And %s <= '%s'", fieldName, endDate);
        }
        return "";
    }

    public static String securityKeyValue(String keyValue){
        String result = ConvertUtils.cStr(keyValue);
        result = result.replace("'", "");
        result = result.replace(";", "");
        result = result.replace("delete", "");
        result = result.replace("truncate", "");
        result = result.replace("update", "");
        result = result.replace("insert", "");
        result = result.replace("create", "");
        result = result.replace("drop", "");
        return result;
    }

    public static void main(String[] args){
        System.out.println(securityKeyValue("'delete,truncate,update,insert,create,drop,;"));
    }
}
