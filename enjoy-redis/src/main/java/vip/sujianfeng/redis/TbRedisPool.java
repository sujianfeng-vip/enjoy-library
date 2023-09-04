package vip.sujianfeng.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vip.sujianfeng.utils.comm.BodyUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.scan.ScannerUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * redis连接池管理
 * Created by sujianfeng on 2013/7/13.
 */
public class TbRedisPool {
    private static Logger logger = LoggerFactory.getLogger(TbRedisPool.class);
    private JedisPool jedisPool = null;
    private JedisPoolConfig jedisPoolConfig;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 构造Redis连接池
     */
    public TbRedisPool(TbRedisConfig redisConfig, JedisPoolConfig jedisPoolConfig){
        this.jedisPoolConfig = jedisPoolConfig;
        this.jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeCount(),
                StringUtilsEx.isNotEmpty(redisConfig.getPassword()) ? redisConfig.getPassword() : null,
                redisConfig.getDatabase());
    }

    /**
     * 使用一个连接完成后要关闭
     * @return
     */
    private synchronized Jedis getJedis(){
        return jedisPool.getResource();
    }

    public <T> T accessJedis(AccessRedis<T> callBack){
        Jedis jedis = getJedis();
        try{
            return callBack.access(jedis);
        }finally {
            closeJedis(jedis);
        }
    }

    public boolean jedisIsOk(Jedis jedis) {
        if (jedis == null){
            return false;
        }
        try {
            return jedis.isConnected() && StringUtilsEx.sameText(jedis.ping(), "PONG");
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public void closeJedis(final Jedis jedis) {
        if (jedis != null && jedisPool !=null) {
            jedis.close();
        }
    }

    public void addCache(String key, Object obj, long expireTime){
        if (key == null || obj == null){
            return;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                String value;
                if (BodyUtils.isPrimitiveDataTypes(obj)){
                    value = String.valueOf(obj);
                }else{
                    value = JSON.toJSONString(obj);
                }
                if (expireTime > 0){
                    jedis.del(key); //要先删除掉，不然加有效期的set会无效
                    jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                }else{
                    jedis.set(key, value);
                }
            }
        } finally{
            closeJedis(jedis);
        }
    }

    public long removeCache(String key) {
        if (key == null){
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                return jedis.del(key);
            }
        }finally{
            closeJedis(jedis);
        }
        return 0;
    }

    public Set<String> getKeys() {
        return getKeys("*");
    }

    public Set<String> getKeys(String patten) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.keys(patten);
        } finally{
            closeJedis(jedis);
        }
        return result;
    }

    public <T> List<T> getList(String key, Class<T> t) {
        if (key == null){
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                String json = jedis.get(key);
                if (json == null){
                    return null;
                }
                return JSON.parseArray(json, t);
            }
        } finally{
            closeJedis(jedis);
        }
        return null;
    }
    public <T> T getObj(String key, Class<T> t) {
        if (key == null){
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                String value = jedis.get(key);
                if (value == null){
                    return null;
                }
                if (ScannerUtils.isAssignableFrom(t, String.class)){
                    return (T) value;
                }
                if (ScannerUtils.isAssignableFrom(t, Integer.class)){
                    return (T) Integer.valueOf(ConvertUtils.cInt(value));
                }
                if (ScannerUtils.isAssignableFrom(t, Long.class)){
                    return (T) ConvertUtils.cLong(value);
                }
                if (ScannerUtils.isAssignableFrom(t, Boolean.class)){
                    return (T) Boolean.valueOf(value);
                }
                return JSON.parseObject(value, t);
            }
        } finally{
            closeJedis(jedis);
        }
        return null;
    }

    public Object flushAll() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                return jedis.flushAll();
            }
        } finally{
            closeJedis(jedis);
        }
        return null;
    }

    public Object flushDb() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                return jedis.flushDB();
            }
        } finally{
            closeJedis(jedis);
        }
        return null;
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_IF_EXIST = "XX";
    private static final String SET_WITH_EXPIRE_TIME = "PX"; //毫秒
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 资源锁定
     * @param lockKey 资源key
     * @param requestId 请求锁定者
     * @param expireTime 锁定过期时间 （单位毫秒）
     * @return
     */
    public boolean lock(String lockKey, String requestId, long expireTime) {
        if (lockKey == null){
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                String s = jedis.get(lockKey);
                if (requestId.equals(s)){
                    //jedis.ttl(lockKey); //获取key的过期时间
                    //续期（如果过期的瞬间，续期会失败）
                    return jedis.expire(lockKey, (int) (expireTime / 1000)) > 0;
                }
                //加锁
                String set = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                return LOCK_SUCCESS.equalsIgnoreCase(set);
            }
        } finally{
            closeJedis(jedis);
        }
        return false;
    }

    /**
     * 资源解锁
     * @param lockKey 资源key
     * @param requestId 解锁者（解锁必须为本人锁定）
     * @return
     */
    public boolean unLock(String lockKey, String requestId) {
        if (lockKey == null){
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                /**
                 * 可以看到，我们解锁只需要两行代码就搞定了！第一行代码，我们写了一个简单的Lua脚本代码。
                 * 第二行代码，我们将Lua代码传到jedis.eval()方法里，并使参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行。
                 * 那么这段Lua代码的功能是什么呢？其实很简单，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。
                 * 那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的。 。
                 * 那么为什么执行eval()方法可以确保原子性，源于Redis的特性，下面是官网对eval命令的部分解释：简单来说，
                 * 就是在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。
                 */
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
                return RELEASE_SUCCESS.equals(result);
            }
        } finally{
            closeJedis(jedis);
        }
        return false;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }
}
