package vip.sujianfeng.utils.comm;

import java.util.*;

/**
 * Data processing tool class
 * Created by sujianfeng on 2014/10/12.
 */
public class DataRowsUtils {

    public static<T> Map<String, T> rows2map(List<T> rows, String keyField) throws IllegalAccessException {
        Map<String, T> result = new HashMap<>();
        for (T row : rows){
            String key = ConvertUtils.cStr(ReflectUtils.getFieldValue(row, keyField));
            if (!result.containsKey(key)){
                result.put(key, row);
            }
        }
        return result;
    }

    public interface IGet{
        String getKey(Object row);
    }

    public static Map rows2map(List rows, IGet callGet){
        Map result = new HashMap<String, Object>();
        for (Object row : rows) {
            String key = callGet.getKey(row);
            result.put(key, row);
        }
        return result;
    }

    public static void pickDiffRows(List<Map<String, Object>> oldRows,
                              List<Map<String, Object>> currRows,
                              String compareField,
                              List<Map<String, Object>> newRows,
                              List<Map<String, Object>> deleteRows) throws IllegalAccessException {
        Map<String, Map<String, Object>> oldMap = rows2map(oldRows, compareField);
        Map<String, Map<String, Object>> currMap = rows2map(currRows, compareField);
        // Delete old data if new data does not exist
        for (Map<String, Object> row : oldRows){
            String keyValue = ConvertUtils.cStr(row.get(compareField));
            if (!currMap.containsKey(keyValue)){
                deleteRows.add(row);
            }
        }
        // The old data department in the new data village is newly added
        for (Map<String, Object> row : currRows){
            String keyValue = ConvertUtils.cStr(row.get(compareField));
            if (!oldMap.containsKey(keyValue)){
                newRows.add(row);
            }
        }
    }

    public static List pickPageRows(List rows, int pageIndex, int pageSize){
        int begin = (pageIndex - 1) * pageSize + 1;
        int end = pageIndex * pageSize;
        List result = new ArrayList<Object>();
        for (int i = 0; i < rows.size(); i++){
            if (i + 1 > end){
                break;
            }
            if (i + 1 >= begin && i + 1 <= end){
                result.add(rows.get(i));
            }
        }
        return result;
    }
}
