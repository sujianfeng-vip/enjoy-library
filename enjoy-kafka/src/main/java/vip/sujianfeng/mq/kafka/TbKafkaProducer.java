package vip.sujianfeng.mq.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.*;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.define.CallResult;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @Author SuJianFeng
 * @Date 2019/5/7 17:37
 **/
public class TbKafkaProducer {

    private KafkaProducer<String, String> producer;
    private Properties properties;
    private String topic;

    public TbKafkaProducer(TbKafkaProducerConfig config, String topic){
        this.properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.put(ProducerConfig.ACKS_CONFIG, config.getAcks());
        properties.put(ProducerConfig.RETRIES_CONFIG, config.getRetries());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getBatchSize());
        properties.put(ProducerConfig.LINGER_MS_CONFIG, config.getLingerMs());
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getBufferMemory());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 3 * 1000);
        //构造 Producer 对象，注意，该对象是线程安全的。
        //一般来说，一个进程内一个 Producer 对象即可。如果想提高性能，可构造多个对象，但最好不要超过 5 个
        this.producer = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    /**
     *
     * 生产消息（可以使用线程池调用，但不要超过5个线程数）（同步）
     * @param opResult
     * @param record
     * @param retryCount 重试次数，如果为0表示不重试
     * @param <T>
     * @return
     */
    public <T> RecordMetadata produce(CallResult<?> opResult, TbKafkaRecord<T> record, int retryCount){
        String value;
        if (record.getData().getClass().isPrimitive()){
            value = ConvertUtils.cStr(record.getData());
        }else{
            value = JSON.toJSONString(record.getData());
        }
        //构造一个消息队列 for Apache Kafka 消息
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(this.topic, record.getKey(), value);
        try {
            //发送消息，并获得一个 Future 对象
            Future<RecordMetadata> metadataFuture = this.producer.send(kafkaMessage);
            //同步获得 Future 对象的结果
            return metadataFuture.get();
        } catch (Exception e) {
            //要考虑重试，参见常见问题: https://help.aliyun.com/document_detail/124136.html
            e.printStackTrace();
            if (retryCount > 0){
                return produce(opResult, record, retryCount - 1);
            }
            opResult.error(e.toString());
            return null;
        }
    }

    /**
     * 生产消息（异步回调）
     * @param opResult
     * @param record
     * @param retryCount
     * @param callback
     * @param <T>
     */
    public <T> void produce(CallResult<?> opResult, TbKafkaRecord<T> record, int retryCount, Callback callback){
        String value;
        if (record.getData().getClass().isPrimitive()){
            value = ConvertUtils.cStr(record.getData());
        }else{
            value = JSON.toJSONString(record.getData());
        }
        //构造一个消息队列 for Apache Kafka 消息
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(this.topic, record.getKey(), value);
        try {
            //发送消息，并通过异步回调
            this.producer.send(kafkaMessage, callback);
        } catch (Exception e) {
            //要考虑重试，参见常见问题: https://help.aliyun.com/document_detail/124136.html
            e.printStackTrace();
            if (retryCount > 0){
                produce(opResult, record, retryCount - 1);
            }
            opResult.error(e.toString());
            return;
        }
    }

    public KafkaProducer<String, String> getProducer() {
        return producer;
    }

    public String getTopic() {
        return topic;
    }

    public Properties getProperties() {
        return properties;
    }
}
