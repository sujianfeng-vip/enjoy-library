package vip.sujianfeng.utils.cache;

import java.util.List;
import java.util.Set;

/**
 * author SuJianFeng
 * createTime  2019/1/16 14:36
 * 缓存操作接口
 **/
public interface IDataCache {
    void addCache(String key, Object obj);
    void addCache(String key, Object obj, int timeout_minutes);
    long removeCache(String key);
    Set<String> getKeys();
    Set<String> getKeys(String prefix);
    <T> T getObj(String key, Class<T> t);
    <T> List<T> getList(String key, Class<T> t);
    Object flushAll();
    Object flushDb();

    boolean lock(String lockKey, String requestId, long expireTime);

    boolean unLock(String lockKey, String requestId);
}
