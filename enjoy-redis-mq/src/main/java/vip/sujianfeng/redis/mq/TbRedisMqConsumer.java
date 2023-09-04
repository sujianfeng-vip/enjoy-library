package vip.sujianfeng.redis.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.redis.TbRedisCache;

/**
 * 消费者
 * @author SuJianFeng
 * @date 2020/9/27 15:49
 **/
public class TbRedisMqConsumer {

    private static Logger logger = LoggerFactory.getLogger(TbRedisMqConsumer.class);

    private TbRedisCache tbRedisCache;

    /**
     * 这个订阅是阻塞的，需要创建一个线程来执行它
     * @param channelName
     * @param t
     * @param event
     * @param <T>
     */
    public <T> void subscribe(String channelName, Class<T> t, TbRedisMqMessageEvent<T> event){
        tbRedisCache.accessJedis(jedis -> {
            jedis.subscribe(new redis.clients.jedis.JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                    try {
                        event.receive(channel, JSON.parseObject(message, t));
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                    }
                }
            }, channelName);
            return null;
        });

    }

    public TbRedisMqConsumer(TbRedisCache tbRedisCache) {
        this.tbRedisCache = tbRedisCache;
    }

    public TbRedisCache getTbRedisCache() {
        return tbRedisCache;
    }

    public void setTbRedisCache(TbRedisCache tbRedisCache) {
        this.tbRedisCache = tbRedisCache;
    }
}
