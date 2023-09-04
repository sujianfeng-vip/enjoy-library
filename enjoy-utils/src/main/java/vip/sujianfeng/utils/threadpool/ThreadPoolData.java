package vip.sujianfeng.utils.threadpool;

/**
 * @author SuJianFeng
 * @date 2022/9/4
 * @Description
 */
public class ThreadPoolData {

    public ThreadPoolData update(long activeCount, long poolSize, long largestPoolSize, long taskCount, long completedTaskCount, long queueSize) {
        this.activeCount = activeCount;
        this.poolSize = poolSize;
        this.largestPoolSize = largestPoolSize;
        this.taskCount = taskCount;
        this.completedTaskCount = completedTaskCount;
        this.queueSize = queueSize;
        return this;
    }

    /**
     * 正在工作的线程数
     */
    private long activeCount;
    /**
     * 当前存在的线程数
     */
    private long poolSize;
    /**
     * 历史最大的线程数
     */
    private long largestPoolSize;
    /**
     * 已提交的任务数
     */
    private long taskCount;
    /**
     * 已完成的任务数
     */
    private long completedTaskCount;
    /**
     * 队列中的任务数
     */
    private long queueSize;

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public long getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(long poolSize) {
        this.poolSize = poolSize;
    }

    public long getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(long largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(long queueSize) {
        this.queueSize = queueSize;
    }
}
