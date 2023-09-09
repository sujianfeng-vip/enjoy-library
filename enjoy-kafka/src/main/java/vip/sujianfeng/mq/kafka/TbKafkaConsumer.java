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
 * author SuJianFeng
 * createTime  2019/5/7 17:43
 **/
public class TbKafkaConsumer {

    private Properties properties;

    private String groupId;
    private List<String> subscribedTopics = new ArrayList<>();

    public TbKafkaConsumer(TbKafkaConsumerConfig config, String groupId, String... topics){
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, config.getSessionTimeoutMs());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getMaxPollRecords());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        this.groupId = groupId;
        for (String topic : topics) {
            this.subscribedTopics.add(topic);
        }
    }

    public <T> void consume(TbKafkaConsume<T> consume, Class<T> t){
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(subscribedTopics);
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                List<TbKafkaRecord<T>> kafkaRecords = new ArrayList<>();
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
                if (consumeResult){
                    consumer.commitSync();
                }
            } catch (Exception e) {
                //https://help.aliyun.com/document_detail/124136.html
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
