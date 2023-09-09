package vip.sujianfeng.datasource;

import java.sql.Connection;

/**
 * author SuJianFeng
 * createTime  2019/12/24 13:26
 **/
public interface DbConnectionAction<T> {
    T consumeAction(Connection connection) throws Exception;
}
