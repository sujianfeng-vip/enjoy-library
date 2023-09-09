package vip.sujianfeng.utils.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author SuJianFeng
 * createTime  2019/2/22 13:15
 **/

public class MemoryCache implements IDataCache{
    private static Map<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public void addCache(String key, Object obj) {
        CACHE_MAP.put(key, obj);
    }

    @Override
    public void addCache(String key, Object obj, int timeout_minutes) {
        CACHE_MAP.put(key, obj);
    }

    @Override
    public long removeCache(String key) {
        if (CACHE_MAP.containsKey(key)){
            CACHE_MAP.remove(key);
            return 1;
        }
        return 0;
    }

    @Override
    public Set<String> getKeys() {
        return CACHE_MAP.keySet();
    }

    @Override
    public Set<String> getKeys(String prefix) {
        Set<String> result = new HashSet<>();
        for (String s : CACHE_MAP.keySet()) {
            if (s.contains(prefix)){
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public <T> T getObj(String key, Class<T> t) {
        return (T) CACHE_MAP.get(key);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> t) {
        return (List<T>) CACHE_MAP.get(key);
    }

    @Override
    public Object flushAll() {
        int size = CACHE_MAP.size();
        CACHE_MAP.clear();
        return size;
    }

    @Override
    public Object flushDb() {
        int size = CACHE_MAP.size();
        CACHE_MAP.clear();
        return size;
    }

    @Override
    public boolean lock(String lockKey, String owner, long expireTime) {
        //暂不支持
        return false;
    }

    @Override
    public boolean unLock(String lockKey, String owner) {
        //暂不支持
        return false;
    }
}
