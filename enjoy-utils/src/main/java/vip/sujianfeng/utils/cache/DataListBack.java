package vip.sujianfeng.utils.cache;

import java.util.List;

/**
 * author sujianfeng
 * create 2019-09-13 7:12
 */
public interface DataListBack<T> {
    List<T> call() throws Exception;
}
