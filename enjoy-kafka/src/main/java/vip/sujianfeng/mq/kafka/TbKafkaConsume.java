package vip.sujianfeng.mq.kafka;

import java.util.List;

/**
 * @author SuJianFeng
 * @date 2019/9/27 14:08
 * 消息消费接口
 **/
public interface TbKafkaConsume<T> {
    /**
     * 批量消费数据
     * @param records
     * @return 是否消费成功
     */
    boolean consume(List<TbKafkaRecord<T>> records);

    /**
     * 消费出错回调
     * @param e
     */
    void onError(Exception e);
}
