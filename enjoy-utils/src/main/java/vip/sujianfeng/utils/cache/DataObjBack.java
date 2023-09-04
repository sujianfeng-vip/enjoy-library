package vip.sujianfeng.utils.cache;

/**
 * @Author 苏建锋
 * @create 2019-09-13 7:12
 */
public interface DataObjBack<T> {
    T call() throws Exception;
}
