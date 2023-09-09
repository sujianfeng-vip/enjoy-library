package vip.sujianfeng.consts;

/**
 * 支付中心-常量定义
 * author SuJianFeng
 * createTime  2022/10/8
 * @Description
 **/
public class PayCenterAppConst {

    /**
     * 支付回调mq通道
     */
    public static final String PAY_BACK_NOTIFY_MQ_CHANNEL = "PAY_BACK_NOTIFY_CHANNEL";

    /**
     * 支付中心redis配置
     */
    public static final String BEAN_NAME_PAY_CENTER_REDIS_CONFIG_PRE = "spring.redis-pay-center";
    public static final String BEAN_NAME_PAY_CENTER_REDIS_CONFIG = "payCenterRedisConfig";
    public static final String BEAN_NAME_PAY_CENTER_REDIS_CACHE = "payCenterRedisCache";

}
