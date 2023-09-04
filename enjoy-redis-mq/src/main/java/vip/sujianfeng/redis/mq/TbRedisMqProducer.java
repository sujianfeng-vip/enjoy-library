package vip.sujianfeng.redis.mq;

import com.alibaba.fastjson.JSON;
import vip.sujianfeng.redis.TbRedisCache;

/**
 * 生产者
 * @author SuJianFeng
 * @date 2020/9/27 15:49
 **/
public class TbRedisMqProducer {

    private TbRedisCache tbRedisCache;

    /**
     * 发布消息
     * @param channelName
     * @param data
     * @param <T>
     */
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
