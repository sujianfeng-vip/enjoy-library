package vip.sujianfeng.utils.comm;

/**
 * @Author SuJianFeng
 * @Date 2019/3/18 15:45
 * 数组克隆
 **/
public class ArrayClone<T> {

    private Class<T> type;

    public ArrayClone(Class<T> type){
        this.type = type;
    }

    public T[][] clone(T[][] arr) {
        return ArrayUtilsEx.clone(arr, type);
    }

    public T[][] reverse(T[][] arr){
        return ArrayUtilsEx.reverse(arr, type);
    }

    public void printArr(T[][] arr){
        ArrayUtilsEx.printArr(arr);
    }

    public static void main(String[] args){
        ArrayClone<Integer> tmp = new ArrayClone<>(Integer.class);
        Integer[][] arr = {
                {11,12,13,14,15},
                {21,22,23,24,25},
                {31,32,33,34,35},
                {41,42,43,44,45},
                {51,52,53,54,55}
        };
        Integer[][] arr1 = tmp.reverse(arr);
        tmp.printArr(arr);
        tmp.printArr(arr1);

    }
}
