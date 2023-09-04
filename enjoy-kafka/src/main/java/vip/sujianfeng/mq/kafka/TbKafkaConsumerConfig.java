package vip.sujianfeng.mq.kafka;

/**
 * @author SuJianFeng
 * @date 2019/11/15 10:47
 * kafka消费者者配置
 **/
public class TbKafkaConsumerConfig {
    private String bootstrapServers;

    //默认值为 30000 ms，可根据自己业务场景调整此值，建议取值不要太小，防止在超时时间内没有发送心跳导致消费者再均衡
    //必须在下次 poll 之前消费完这些数据, 且总耗时不得超过 SESSION_TIMEOUT_MS_CONFIG 的值
    //建议开一个单独的线程池来消费消息，然后异步返回结果
    private int sessionTimeoutMs = 25000;
    //每次 poll 的最大数量
    //注意该值不要改得太大，如果 poll 太多数据，而不能在下次 poll 之前消费完，则会触发一次负载均衡，产生卡顿
    private int maxPollRecords = 30;

    //消息的反序列化方式
    private String keySerializer = "org.apache.kafka.common.serialization.StringDeserializer";
    private String valueSerializer = "org.apache.kafka.common.serialization.StringDeserializer";

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }
}
