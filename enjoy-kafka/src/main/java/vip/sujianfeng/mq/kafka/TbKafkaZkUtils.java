package vip.sujianfeng.mq.kafka;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Properties;

/**
 * author SuJianFeng
 * createTime  2019/5/7 17:05
 **/
public class TbKafkaZkUtils {
    private static ZkUtils getZkUtils(String servers){
        return ZkUtils.apply(servers, 30000, 30000, JaasUtils.isZkSecurityEnabled());
    }

    public static boolean topicExists(String servers, TbKafkaTopicBean topic){
        ZkUtils zkUtils = getZkUtils(servers);
        return AdminUtils.topicExists(zkUtils, topic.getTopicName());
    }

    public static void createKafaTopic(String servers, TbKafkaTopicBean topic) {
        ZkUtils zkUtils = getZkUtils(servers);
        AdminUtils.createTopic(zkUtils, topic.getTopicName(),  topic.getPartition(),
                topic.getReplication(),  new Properties(), new RackAwareMode.Enforced$());
        zkUtils.close();
    }

    public static void deleteKafaTopic(String servers, TbKafkaTopicBean topic) {
        ZkUtils zkUtils = getZkUtils(servers);
        AdminUtils.deleteTopic(zkUtils, topic.getTopicName());
        zkUtils.close();
    }
}
