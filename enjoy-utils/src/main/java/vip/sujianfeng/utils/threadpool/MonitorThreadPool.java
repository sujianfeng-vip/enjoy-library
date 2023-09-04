package vip.sujianfeng.utils.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SuJianFeng
 * @date 2022/9/4
 * @Description
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
     * 每次执行任务前调用
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        //monitor();
    }

    /**
     * 每次任务完成后调用
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        monitor();
    }

    /**
     * 线程池关闭前调用
     */
    @Override
    protected void terminated() {
        //monitor();
    }

    /**
     * 监控线程池情况
     */
    public void monitor() {
        this.monitorEvent.monitor(this.threadPoolData.update(getActiveCount(), getPoolSize(), getLargestPoolSize(), getTaskCount(), getCompletedTaskCount(), getQueue().size()));
    }

}