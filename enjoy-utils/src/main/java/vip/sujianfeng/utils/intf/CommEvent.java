package vip.sujianfeng.utils.intf;

/**
 * 通用触发事件
 * @Author SuJianFeng
 * @Date 2022/12/14
 * @Description
 **/
public interface CommEvent<T> {
    void call(T obj);
}
