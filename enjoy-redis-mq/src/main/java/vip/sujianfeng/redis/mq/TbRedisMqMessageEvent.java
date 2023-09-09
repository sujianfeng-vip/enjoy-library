package vip.sujianfeng.redis.mq;

/**
 * author SuJianFeng
 * createTime  2020/9/27 15:53
 **/
public interface TbRedisMqMessageEvent<T> {
    void receive(String channel, T data) throws Exception;
}
