package vip.sujianfeng.utils.intf;

public interface CommEvent<T> {
    void call(T obj);
}
