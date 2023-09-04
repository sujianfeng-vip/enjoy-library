package vip.sujianfeng.utils.cache;

import java.util.List;
import java.util.Set;

/**
 * 缓存控制
 * Created by sujianfeng on 2014/9/29.
 */
public class DataCacheHandler {
    private IDataCache dataCache;

    public DataCacheHandler(IDataCache dataCache) {
        this.dataCache = dataCache;
    }

    public IDataCache getDataCache() {
        return dataCache;
    }

    public void setDataCache(IDataCache dataCache) {
        this.dataCache = dataCache;
    }

    public void addCache(String key, Object obj, int timeout_minutes){
        if(key == null || obj == null){
            return;
        }
        dataCache.addCache(key, obj, timeout_minutes);
    }

    public void addCache(String key, Object obj){
        addCache(key, obj, 0);
    }

    public long removeCache(String key){
        if(key == null){
            return 0;
        }
        return dataCache.removeCache(key);
    }

    /**
     * 清除所有库缓存
     */
    public void flushAll(){
        dataCache.flushAll();
    }

    /**
     * 清除当前库缓存
     */
    public void flushDb(){
        dataCache.flushDb();
    }

    public int removeCacheSub(String sub) {
        //System.out.println("removeCacheSub: " + sub);
        Set<String> items = dataCache.getKeys();
        if (items == null){
            return 0;
        }
        int count = 0;
        for (String item : items){
            //System.out.println(item);
            if (item.indexOf(sub) >= 0){
                count += removeCache(item);
            }
        }
        return count;
    }

    /**
     * 暂存数据，用于提供执行效率
     * 注意点：当数据变化时，要清除或更新暂存数据
     * @param key
     * @param callBack
     * @return
     */
    public <T> T getOrBuildCacheObj(String key, Class<T> t, DataObjBack<T> callBack, int timeout_minutes) throws Exception {
        if(key == null){
            return null;
        }
        T obj = getCacheObj(key, t);
        if (obj == null){
            if (callBack == null){
                return null;
            }
            obj = callBack.call();
            if (obj != null){
                addCache(key, obj, timeout_minutes);
            }
        }
        return obj;
    }

    public <T> List<T> getOrBuildCacheList(String key, Class<T> t, DataListBack<T> callBack, int timeout_minutes) throws Exception {
        if(key == null){
            return null;
        }
        List<T> list = getCacheList(key, t);
        if (list == null){
            if (callBack == null){
                return null;
            }
            list = callBack.call();
            if (list != null){
                addCache(key, list, timeout_minutes);
            }
        }
        return list;
    }

    public <T> T getOrBuildCacheObj(String key, Class<T> t, int timeOutMinutes, DataObjBack<T> callBack) throws Exception {
        return getOrBuildCacheObj(key, t, callBack, timeOutMinutes);
    }

    public <T> List<T> getOrBuildCacheList(String key, Class<T> t, int timeOutMinutes, DataListBack<T> callBack) throws Exception {
        return getOrBuildCacheList(key, t, callBack, timeOutMinutes);
    }

    public <T> T getCacheObj(String key, Class<T> t){
        return dataCache.getObj(key, t);
    }

    public <T> List<T> getCacheList(String key, Class<T> t){
        return dataCache.getList(key, t);
    }


    public Set<String> getKeys(String pattern){
        return dataCache.getKeys(pattern);
    }


}
