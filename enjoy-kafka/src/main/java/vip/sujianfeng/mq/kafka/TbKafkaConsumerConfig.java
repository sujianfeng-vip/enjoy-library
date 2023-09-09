package vip.sujianfeng.mq.kafka;

/**
 * author SuJianFeng
 * createTime  2019/11/15 10:47
 **/
public class TbKafkaConsumerConfig {
    private String bootstrapServers;


    private int sessionTimeoutMs = 25000;

    private int maxPollRecords = 30;

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
