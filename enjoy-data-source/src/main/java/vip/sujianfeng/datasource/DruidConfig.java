package vip.sujianfeng.datasource;

/**
 * author SuJianFeng
 * createTime  2019/12/24 10:29
 **/
public class DruidConfig {

    public String toKey(){
        return String.format("%s/%s/%s/%s", driverClassName, url, username, password);
    }

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String database;
    private String initialSize = "0";
    private String maxActive = "500";
    private String minIdle = "0";
    private String maxWait = "-1";

    /**
     * 超过时间限制时回收连接:
     * 当程序存在缺陷时，申请的连接忘记关闭，这时候，就存在连接泄漏了。
     * Druid提供了RemoveAbandanded相关配置，用来关闭长时间不使用的连接。
     * 注：配置removeAbandoned对性能会有一些影响，建议怀疑存在泄漏之后再打开。
     * 在上面的配置中，如果连接超过30分钟未关闭，就会被强行回收，并且日志记录连接申请时的调用堆栈
     */
    private String removeAbandoned = "false";
    private String removeAbandonedTimeout = String.valueOf(60 * 3600);
    private String logAbandoned = "true";

    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select ‘X’。
     * 如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
     */
    private String validationQuery = "select 1";
    /**
     * 单位：秒，检测连接是否有效的超时时间。
     * 底层调用jdbc Statement对象的void setQueryTimeout(int seconds)方法。
     */
    private String validationQueryTimeout = "-1";
    /**
     * 默认值为true。申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
     */
    private String testOnBorrow = "true";
    /**
     * 默认值为false。归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
     */
    private String testOnReturn = "true";
    /**
     * 默认值为false。建议配置为true，不影响性能，并且保证安全性。
     * 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
     */
    private String testWhileIdle = "true";

    /**
     * 1.0.28版本，默认值为false。
     * 连接池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作。
     */
    private String keepAlive = "false";

    /**
     * 1.0.14版本，默认值为1分钟，单位毫秒。
     * 有两个含义：一个是Destroy线程会检测连接的间隔时间，
     * 如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接；
     * 另一个是testWhileIdle的判断依据，详细看testWhileIdle属性的说明。
     */
    private String timeBetweenEvictionRunsMillis = String.valueOf(1000 * 60L);

    /**
     * 连接保持空闲而不被驱逐的最小时间，单位毫秒。
     */
    private String minEvictableIdleTimeMillis = String.valueOf(1000L * 60L * 30L);

    /**
     *  物理连接初始化的时候执行的sql。
     */
    private String connectionInitSqls = null;

    /**
     *  默认根据dbType自动识别。当数据库抛出一些不可恢复的异常时，抛弃连接。
     */
    private String exceptionSorter = null;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    public String getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(String removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public String getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(String removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public String getLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(String logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public void setValidationQueryTimeout(String validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(String testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(String timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public String getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(String minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getConnectionInitSqls() {
        return connectionInitSqls;
    }

    public void setConnectionInitSqls(String connectionInitSqls) {
        this.connectionInitSqls = connectionInitSqls;
    }

    public String getExceptionSorter() {
        return exceptionSorter;
    }

    public void setExceptionSorter(String exceptionSorter) {
        this.exceptionSorter = exceptionSorter;
    }
}
