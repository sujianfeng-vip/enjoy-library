package vip.sujianfeng.mq.kafka;

import java.util.List;

/**
 * author SuJianFeng
 * createTime  2019/9/27 14:08
 **/
public interface TbKafkaConsume<T> {

    boolean consume(List<TbKafkaRecord<T>> records);

    void onError(Exception e);
}
