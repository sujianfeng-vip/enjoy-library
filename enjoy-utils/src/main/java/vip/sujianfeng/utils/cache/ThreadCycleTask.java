package vip.sujianfeng.utils.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Single threaded timed task
 * author SuJianFeng
 * createTime  2022/9/23
 * Description Used for single threaded cycle execution of specified tasks
 **/
public abstract class ThreadCycleTask {
    private String name;
    /**
     * Whether to output logs
     */
    private boolean showLog = true;
    /**
     * Interval time
     */
    private long intervalSeconds = 60;
    /**
     * Delay initialization time
     */
    private long delayInitSeconds = 10;

    public abstract void cycleExecute() throws Exception;

    protected void init() {

    }

    public ThreadCycleTask(String name) {
        this.name = name;
        threadSyncData.execute(()->{
            try {
                Thread.sleep(1000 * delayInitSeconds);
            } catch (InterruptedException e) {
                logger.error(e.toString(), e);
            }
            this.init();
            if (showLog) logger.info("The thread timing task [{}] has been initialized!", this.name);
            while (true) {
                try {
                    if (showLog) logger.info("Thread timed task [{}], starting execution", this.name);
                    this.cycleExecute();
                    if (showLog) logger.info("Thread timed task [{}], execution completed!", this.name);
                    if (intervalSeconds < 1) {
                        intervalSeconds = 1;
                    }
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                }
                try {
                    Thread.sleep(1000 * intervalSeconds);
                } catch (InterruptedException e) {
                    logger.error(e.toString(), e);
                }
            }
        });
    }

    public long getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(long intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public long getDelayInitSeconds() {
        return delayInitSeconds;
    }

    public void setDelayInitSeconds(int delayInitSeconds) {
        this.delayInitSeconds = delayInitSeconds;
    }

    public ThreadPoolExecutor getThreadSyncData() {
        return threadSyncData;
    }

    public void setThreadSyncData(ThreadPoolExecutor threadSyncData) {
        this.threadSyncData = threadSyncData;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private ThreadPoolExecutor threadSyncData = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new ArrayBlockingQueue(1), new ThreadPoolExecutor.DiscardPolicy());
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }
}
