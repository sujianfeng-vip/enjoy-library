package vip.sujianfeng.utils.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author SuJianFeng
 * createTime  2022/9/4
 */
public class MonitorThreadPool extends ThreadPoolExecutor {

    public MonitorThreadPool(int corePoolSize, int maximumPoolSize,
                             long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue, RejectedExecutionHandler threadFactory, IMonitorEvent monitorEvent) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.monitorEvent = monitorEvent;
    }

    private IMonitorEvent monitorEvent;

    private ThreadPoolData threadPoolData = new ThreadPoolData();

    /**
     * Called before each task execution
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        //monitor();
    }

    /**
     * Called after each task is completed
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        monitor();
    }

    /**
     * Called before the thread pool is closed
     */
    @Override
    protected void terminated() {
        //monitor();
    }

    /**
     * Monitoring Thread Pool Status
     */
    public void monitor() {
        this.monitorEvent.monitor(this.threadPoolData.update(getActiveCount(), getPoolSize(), getLargestPoolSize(), getTaskCount(), getCompletedTaskCount(), getQueue().size()));
    }

}