package vip.sujianfeng.redis;

import redis.clients.jedis.JedisPoolConfig;
import vip.sujianfeng.utils.cache.IDataCache;

import java.util.List;
import java.util.Set;

/**
 * author sujianfeng
 * create 2019-10-17 5:44
 */
public class TbRedisCache implements IDataCache {

    private TbRedisPool redisPool;

    public TbRedisCache(TbRedisConfig redisConfig, JedisPoolConfig jedisPoolConfig){
        this.redisPool = new TbRedisPool(redisConfig, jedisPoolConfig);
    }

    public <T> T accessJedis(AccessRedis<T> callBack){
        return this.redisPool.accessJedis(callBack);
    }

    @Override
    public void addCache(String key, Object obj) {
        addCache(key, obj, 0);
    }

    @Override
    public void addCache(String key, Object obj, int timeout_minutes) {
        this.redisPool.addCache(key, obj, timeout_minutes * 60 * 1000);
    }

    @Override
    public long removeCache(String key) {
        return this.redisPool.removeCache(key);
    }

    @Override
    public Set<String> getKeys() {
        return this.redisPool.getKeys();
    }

    @Override
    public Set<String> getKeys(String patten) {
        return this.redisPool.getKeys(patten);
    }

    @Override
    public <T> T getObj(String key, Class<T> t) {
        return this.redisPool.getObj(key, t);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> t) {
        return this.redisPool.getList(key, t);
    }

    @Override
    public Object flushAll() {
        return this.redisPool.flushAll();
    }

    @Override
    public Object flushDb() {
        return this.redisPool.flushDb();
    }

    @Override
    public boolean lock(String lockKey, String requestId, long expireTime) {
        return this.redisPool.lock(lockKey, requestId, expireTime);
    }

    @Override
    public boolean unLock(String lockKey, String requestId) {
        return this.redisPool.unLock(lockKey, requestId);
    }

    public TbRedisPool getRedisPool() {
        return redisPool;
    }
}
