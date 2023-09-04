package vip.sujianfeng.datasource;

import java.sql.Connection;

/**
 * @author SuJianFeng
 * @date 2019/12/24 13:26
 * 消费数据库连接事件
 **/
public interface DbConnectionAction<T> {
    T consumeAction(Connection connection) throws Exception;
}
