package vip.sujianfeng.utils.cache;

import java.util.List;
import java.util.Set;

/**
 * @Author SuJianFeng
 * @Date 2019/1/16 14:36
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

    /**
     * 资源锁定
     * @param lockKey 资源key
     * @param requestId 请求锁定者
     * @param expireTime 锁定过期时间
     * @return
     */
    boolean lock(String lockKey, String requestId, long expireTime);

    /**
     * 资源解锁
     * @param lockKey 资源key
     * @param requestId 解锁者（解锁必须为本人锁定）
     * @return
     */
    boolean unLock(String lockKey, String requestId);
}
