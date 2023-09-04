package vip.sujianfeng.mq.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author SuJianFeng
 * @Date 2019/5/7 17:43
 **/
public class TbKafkaConsumer {

    private Properties properties;

    private String groupId;

    //设置  Consumer Group 订阅的 Topic，可订阅多个 Topic。如果 GROUP_ID_CONFIG 相同，那建议订阅的 Topic 设置也相同
    //每个 Topic 需要先在控制台进行创建
    private List<String> subscribedTopics = new ArrayList<>();

    public TbKafkaConsumer(TbKafkaConsumerConfig config, String groupId, String... topics){
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, config.getSessionTimeoutMs());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getMaxPollRecords());
        //消息的反序列化方式
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        //当前消费实例所属的 Consumer Group，请在控制台创建后填写
        //属于同一个 Consumer Group 的消费实例，会负载消费消息
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        //消费一批后，不自动提交已经消费标记，由程序手动控制已消费
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        this.groupId = groupId;
        for (String topic : topics) {
            this.subscribedTopics.add(topic);
        }
    }

    /**
     * 消息消费（建议使用线程池调用）
     * @param consume
     * @param t
     * @param <T>
     */
    public <T> void consume(TbKafkaConsume<T> consume, Class<T> t){
        //构造消息对象，即生成一个消费实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(subscribedTopics);
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                List<TbKafkaRecord<T>> kafkaRecords = new ArrayList<>();
                //必须在下次 poll 之前消费完这些数据, 且总耗时不得超过 SESSION_TIMEOUT_MS_CONFIG 的值
                //建议开一个单独的线程池来消费消息，然后异步返回结果
                for (ConsumerRecord<String, String> record : records) {
                    T obj;
                    try{
                        if (t.isPrimitive()){
                            obj = (T)record.value();
                        }else{
                            obj = JSON.parseObject(record.value(), t);
                        }
                        TbKafkaRecord<T> item = new TbKafkaRecord<>(record.key(), obj);
                        kafkaRecords.add(item);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                boolean consumeResult = consume.consume(kafkaRecords);
                //消费成功
                if (consumeResult){
                    //提交消费成功
                    consumer.commitSync();
                }
            } catch (Exception e) {
                //参见常见问题: https://help.aliyun.com/document_detail/124136.html
                e.printStackTrace();
                consume.onError(e);
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<String> getSubscribedTopics() {
        return subscribedTopics;
    }
}
