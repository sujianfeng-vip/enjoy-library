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
 * Created by sujianfeng on 2013/7/13.
 */
public class TbRedisPool {
    private static Logger logger = LoggerFactory.getLogger(TbRedisPool.class);
    private JedisPool jedisPool = null;
    private JedisPoolConfig jedisPoolConfig;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public TbRedisPool(TbRedisConfig redisConfig, JedisPoolConfig jedisPoolConfig){
        this.jedisPoolConfig = jedisPoolConfig;
        this.jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeCount(),
                StringUtilsEx.isNotEmpty(redisConfig.getPassword()) ? redisConfig.getPassword() : null,
                redisConfig.getDatabase());
    }

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
                    jedis.del(key);
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
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

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
                    //jedis.ttl(lockKey);
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

    public boolean unLock(String lockKey, String requestId) {
        if (lockKey == null){
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null){
                /**
                 *As you can see, we only need two lines of code to unlock it! The first line of code, we wrote a simple Lua script code.
                 *In the second line of code, we pass Lua code to the jedis. eval() method and assign the parameter KEYS [1] to lockKey and ARGV [1] to requestId. The eval() method hands over Lua code to the Redis server for execution.
                 *So what is the function of this Lua code? It's actually quite simple. First, obtain the value value corresponding to the lock, check if it is equal to requestId, and if it is equal, delete the lock (unlock).
                 *So why use Lua language for implementation? Because it is important to ensure that the above operations are atomic.
                 *So why can executing the eval() method ensure atomicity, derived from the characteristics of Redis? Here is a partial explanation of the eval command on the official website: Simply put,
                 *When the eval command executes Lua code, the Lua code will be executed as a command, and Redis will not execute other commands until the eval command is completed.
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
