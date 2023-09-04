package vip.sujianfeng.utils.comm;

import java.util.*;

/**
 * 数据处理工具类
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

    /**
     * 根据比较字段，找出差异部分（新增、删除）
     * @param oldRows   原数据
     * @param currRows   处理后数据
     * @param compareField 比较自动
     * @param newRows 新增行
     * @param deleteRows 被删除行
     */
    public static void pickDiffRows(List<Map<String, Object>> oldRows,
                              List<Map<String, Object>> currRows,
                              String compareField,
                              List<Map<String, Object>> newRows,
                              List<Map<String, Object>> deleteRows) throws IllegalAccessException {
        Map<String, Map<String, Object>> oldMap = rows2map(oldRows, compareField);
        Map<String, Map<String, Object>> currMap = rows2map(currRows, compareField);
        //旧数据存在新数据不存在的为删除
        for (Map<String, Object> row : oldRows){
            String keyValue = ConvertUtils.cStr(row.get(compareField));
            if (!currMap.containsKey(keyValue)){
                deleteRows.add(row);
            }
        }
        //新数据村旧数据部存在的为新增
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
