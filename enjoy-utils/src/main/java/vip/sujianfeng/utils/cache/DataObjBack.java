package vip.sujianfeng.utils.cache;

/**
 * author sujianfeng
 * create 2019-09-13 7:12
 */
public interface DataObjBack<T> {
    T call() throws Exception;
}
