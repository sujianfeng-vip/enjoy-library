package vip.sujianfeng.mq.kafka;

/**
 * author SuJianFeng
 * createTime  2019/5/7 17:04
 **/
public class TbKafkaTopicBean {

    private String topicName;       // topic 名称
    private Integer partition;      // partition 分区数量
    private Integer replication;    // replication 副本数量
    private String descrbe;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public Integer getReplication() {
        return replication;
    }

    public void setReplication(Integer replication) {
        this.replication = replication;
    }

    public String getDescrbe() {
        return descrbe;
    }

    public void setDescrbe(String descrbe) {
        this.descrbe = descrbe;
    }

    @Override
    public String toString() {
        return "KafkaTopicBean [topicName=" + topicName + ", partition=" + partition
                + ", replication=" + replication + ", descrbe=" + descrbe +"]";
    }

}
