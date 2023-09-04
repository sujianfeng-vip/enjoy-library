package vip.sujianfeng.utils.scan;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author SuJianFeng
 * @date 2019/8/26 17:20
 **/
public interface Scan {
    List<String> search(ScanFilter filter);
}
