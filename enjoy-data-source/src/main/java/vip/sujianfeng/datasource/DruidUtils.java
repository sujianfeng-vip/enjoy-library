package vip.sujianfeng.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author SuJianFeng
 * createTime  2019/12/6 14:36
 **/
public class DruidUtils extends DruidDataSource {

    public static DruidDataSource getDruidDataSource(DruidConfig dbConfig) throws Exception {
        DruidDataSource result = MY_DRUID_DATA_SOURCE_MAP.get(dbConfig.toKey());
        if (result == null){
            JSONObject configMap = JSON.parseObject(JSON.toJSONString(dbConfig));
            result = (DruidDataSource) DruidDataSourceFactory.createDataSource(configMap);
            MY_DRUID_DATA_SOURCE_MAP.put(dbConfig.toKey(), result);
        }
        return result;
    }

    private DruidUtils(){}

    private static Map<String, DruidDataSource> MY_DRUID_DATA_SOURCE_MAP = new ConcurrentHashMap<>();

}
