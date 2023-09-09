package vip.sujianfeng.mq.kafka;

/**
 * author SuJianFeng
 * createTime  2019/11/17 7:52
 **/
public class TbKafkaRecord<T> {

    public TbKafkaRecord(String key, T data) {
        this.key = key;
        this.data = data;
    }

    private String key;
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
