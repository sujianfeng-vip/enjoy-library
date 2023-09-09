package vip.sujianfeng.redis.mq;

import com.alibaba.fastjson.JSON;
import vip.sujianfeng.redis.TbRedisCache;

/**
 * author SuJianFeng
 * createTime  2020/9/27 15:49
 **/
public class TbRedisMqProducer {

    private TbRedisCache tbRedisCache;

    public <T> Long publish(String channelName, T data){
        String msg = JSON.toJSONString(data);
        return tbRedisCache.accessJedis(jedis -> {
            return jedis.publish(channelName, msg);
        });
    }

    public TbRedisMqProducer(TbRedisCache tbRedisCache) {
        this.tbRedisCache = tbRedisCache;
    }

    public TbRedisCache getTbRedisCache() {
        return tbRedisCache;
    }

    public void setTbRedisCache(TbRedisCache tbRedisCache) {
        this.tbRedisCache = tbRedisCache;
    }
}
