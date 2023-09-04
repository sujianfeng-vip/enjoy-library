package vip.sujianfeng.utils.comm;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;

/**
 * @Author SuJianFeng
 * @Date 2019/3/18 15:45
 * 二维数组工具类
 **/
public class ArrayUtilsEx {
    private static Logger logger = LoggerFactory.getLogger(ArrayUtilsEx.class);
    public static<T> T[][] clone(T[][] arr, Class<T> type){
        try{
            if (arr == null){
                return null;
            }
            if (arr.length == 0){
                return (T[][]) Array.newInstance(type);
            }
            int rowSize = arr.length;
            if (arr[0].length == 0){
                return (T[][]) Array.newInstance(type);
            }
            int colSize = arr[0].length;
            T[][] result = (T[][]) Array.newInstance(type, rowSize, colSize);
            for (int i = 0; i < rowSize; i++){
                for (int j = 0; j < colSize; j++){
                    result[i][j] = arr[i][j];
                }
            }
            return result;
        }catch (Exception e){
            logger.error(e.toString(), e);
        }
        return null;
    }

    public static<T> T[][] reverse(T[][] arr, Class<T> type){
        T[][] result = clone(arr, type);
        for (int i = 0; i < result.length; i++){
            ArrayUtils.reverse(result[i]);
        }
        ArrayUtils.reverse(result);
        return result;
    }

    public static<T> boolean equals(T[][] arr, T[][] arr1){
        if (arr.length != arr1.length){
            return false;
        }
        for (int y = 0; y < arr.length; y++){
            T[] row = arr[y];
            T[] row1 = arr1[y];
            if (row.length != row1.length){
                return false;
            }
            for (int x = 0; x < row.length; x++){
                if (row[x] == null && row1[x] != null){
                    return false;
                }
                if (row[x] != null && row1[x] == null){
                    return false;
                }
                if (!row[x].equals(row1[x])){
                    return false;
                }
            }
        }
        return true;
    }

    public static<T> void printArr(T[][] arr){
        StringBuilderEx sb = new StringBuilderEx();
        for (T[] row : arr){
            for (T value: row){
                sb.append(value).append(",");
            }
            sb.appendRow("");
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args){
        Integer[][] arr = {
                {11,12,13,14,15},
                {21,22,23,24,25},
                {31,32,33,34,35},
                {41,42,43,44,45},
                {51,52,53,54,55}
        };
        Integer[][] arr1 = reverse(arr, Integer.class);
        printArr(arr);
        printArr(arr1);

        String[][] arr3 = {
                {"a1","a2","a3","a4","a5"},
                {"b1","b2","b3","b4","b5"},
                {"c1","c2","c3","c4","c5"},
                {"d1","d2","d3","d4","d5"},
                {"e1","e2","e3","e4","e5"}
        };
        String[][] arr4 = reverse(arr3, String.class);
        printArr(arr3);
        printArr(arr4);

    }
}
