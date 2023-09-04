package vip.sujianfeng.utils;

import vip.sujianfeng.utils.comm.CompareObjectUtils;
import vip.sujianfeng.utils.models.Obj1;
import vip.sujianfeng.utils.models.Obj2;

import java.util.Arrays;

public class CompareUtilsTest {

    public static void main(String[] args) {

//        CompareUtils.isSame(new Obj1("11", "22"), new Obj2("11", "22"), Arrays.asList("f1", "f2"), Arrays.asList("p1", "p2"));
//        !CompareUtils.isSame(new Obj1("11", "22"), new Obj2("aa", "bb"), Arrays.asList("f1", "f2"), Arrays.asList("p1", "p2"));
        boolean same = CompareObjectUtils.isSame(new Obj1("{\"aa\":\"11\",\"bb\":\"22\"}", "[{\"aa\":\"11\",\"bb\":\"22\"},{\"aa\":\"33\",\"bb\":\"44\"}]"),
                new Obj2("{\"bb\":\"22\",\"aa\":\"11\"}", "[{\"aa\":\"11\",\"bb\":\"22\"},{\"aa\":\"33\",\"bb\":\"44\"}]"),
                Arrays.asList("f1", "f2"), Arrays.asList("p1", "p2"));
        System.out.println("测试结果：" + same);
    }
}
