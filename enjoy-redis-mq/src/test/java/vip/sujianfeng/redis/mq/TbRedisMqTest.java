package vip.sujianfeng.redis.mq;


import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPoolConfig;
import vip.sujianfeng.redis.TbRedisCache;
import vip.sujianfeng.redis.TbRedisConfig;

/**
 * @author SuJianFeng
 * @date 2020/9/27 16:09
 **/
public class TbRedisMqTest {

    private TbRedisCache tbRedisCache;

    @BeforeEach
    public void beforeTest(){
        TbRedisConfig redisConfig = new TbRedisConfig();
        redisConfig.setDatabase(0);
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        redisConfig.setPassword("");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.tbRedisCache = new TbRedisCache(redisConfig, poolConfig);
    }

    @Test
    public void test() throws InterruptedException {
        String chanelName = "c1";
        TempObj aaa = new TempObj(1, "打雷啦！下雨啦！起来收衣服啦！");
        TbRedisMqProducer producer = new TbRedisMqProducer(tbRedisCache);
        TbRedisMqConsumer consumer = new TbRedisMqConsumer(tbRedisCache);
        new Thread(()->{
            consumer.subscribe(chanelName, TempObj.class, (chanel, data)->{
                System.out.printf("\n订阅1 -> 收到消息: %s%n", JSON.toJSONString(data));
            });
        }).start();
        new Thread(()->{
            consumer.subscribe(chanelName, TempObj.class, (chanel, data)->{
                System.out.printf("\n订阅2 -> 收到消息: %s%n", JSON.toJSONString(data));
            });
        }).start();

        new Thread(()->{
            consumer.subscribe(chanelName, TempObj.class, (chanel, data)->{
                System.out.printf("\n订阅3 -> 收到消息: %s%n", JSON.toJSONString(data));
            });
        }).start();

        producer.publish(chanelName, aaa);
        Thread.sleep(5000);
    }
}
