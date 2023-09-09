package vip.sujianfeng.mq.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.*;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.define.CallResult;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * author SuJianFeng
 * createTime  2019/5/7 17:37
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
        this.producer = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    public <T> RecordMetadata produce(CallResult<?> opResult, TbKafkaRecord<T> record, int retryCount){
        String value;
        if (record.getData().getClass().isPrimitive()){
            value = ConvertUtils.cStr(record.getData());
        }else{
            value = JSON.toJSONString(record.getData());
        }
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(this.topic, record.getKey(), value);
        try {
            Future<RecordMetadata> metadataFuture = this.producer.send(kafkaMessage);
            return metadataFuture.get();
        } catch (Exception e) {
            // https://help.aliyun.com/document_detail/124136.html
            e.printStackTrace();
            if (retryCount > 0){
                return produce(opResult, record, retryCount - 1);
            }
            opResult.error(e.toString());
            return null;
        }
    }

    public <T> void produce(CallResult<?> opResult, TbKafkaRecord<T> record, int retryCount, Callback callback){
        String value;
        if (record.getData().getClass().isPrimitive()){
            value = ConvertUtils.cStr(record.getData());
        }else{
            value = JSON.toJSONString(record.getData());
        }
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(this.topic, record.getKey(), value);
        try {
            this.producer.send(kafkaMessage, callback);
        } catch (Exception e) {
            // https://help.aliyun.com/document_detail/124136.html
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
