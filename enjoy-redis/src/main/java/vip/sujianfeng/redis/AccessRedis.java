package vip.sujianfeng.redis;

import redis.clients.jedis.Jedis;

/**
 * @Author SuJianFeng
 * @create 2020-08-29 7:54
 */
public interface AccessRedis<T> {
    T access(Jedis jedis);
}
