package vip.sujianfeng.mq.kafka;

import org.testng.annotations.Test;
import vip.sujianfeng.utils.define.CallResult;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author SuJianFeng
 * @Date 2019/5/7 17:03
 **/
public class KafkaTest {
    public static ThreadPoolExecutor ThreadWorker100 = new ThreadPoolExecutor(
            50, //正式工数量,
            100, // 工人数量上限，包括正式工和临时工
            10, TimeUnit.SECONDS, // 临时工游手好闲的最长时间，超过这个时间将被解雇
            new ArrayBlockingQueue<>(100), // 使用有界队列，避免OOM
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Test
    public void test(){
        //zookeeper地址：端口号
        String servers = "47.110.199.57:9092";
        String topic = "STC-MS-TRACE-LOG-TEST";
        String groupId = "STC-MS-TRACE-LOG-TEST";
        //topic对象
        /*
        TbKafkaTopicBean topic = new TbKafkaTopicBean();
        topic.setTopicName(topicStr);  //topic名称
        topic.setPartition(1);            //分区数量设置为1
        topic.setReplication(1);        //副本数量设置为1

        if (!TbKafkaZkUtils.topicExists(servers, topic)){
            //创建topic
            TbKafkaZkUtils.createKafaTopic(servers, topic);
        }
        //删除topic
        //KafkaUtils.deleteKafaTopic(ZkStr, topic);
        */

        TbKafkaConsumerConfig consumerConfig = new TbKafkaConsumerConfig();
        consumerConfig.setBootstrapServers(servers);
        TbKafkaConsumer myKafkaConsumer = new TbKafkaConsumer(consumerConfig, groupId, topic);

        ThreadWorker100.execute(()->{
            myKafkaConsumer.consume(new TbKafkaConsume<String>() {

                @Override
                public boolean consume(List<TbKafkaRecord<String>> list) {
                    System.out.println(String.format("订阅到数据%s条....", list.size()));
                    for (TbKafkaRecord<String> record : list) {
                        System.out.println(String.format("%s -> %s", record.getKey(), record.getData()));
                    }
                    return true;
                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            }, String.class);
        });


        TbKafkaProducerConfig producerConfig = new TbKafkaProducerConfig();
        producerConfig.setBootstrapServers(servers);
        TbKafkaProducer myKafkaProducer = new TbKafkaProducer(producerConfig, topic);

        CallResult<?> opResult = new CallResult<>();
        for (int i = 0; i < 20; i++){
            myKafkaProducer.produce(opResult, new TbKafkaRecord<>("key" + i, "value" + i), 0);
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
