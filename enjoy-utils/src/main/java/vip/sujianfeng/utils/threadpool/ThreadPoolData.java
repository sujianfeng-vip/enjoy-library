package vip.sujianfeng.utils.threadpool;

/**
 * author SuJianFeng
 * createTime  2022/9/4
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
     * Number of working threads
     */
    private long activeCount;
    /**
     * Current number of threads present
     */
    private long poolSize;
    /**
     * The maximum number of threads in history
     */
    private long largestPoolSize;
    /**
     * Number of tasks submitted
     */
    private long taskCount;
    /**
     * Number of completed tasks
     */
    private long completedTaskCount;
    /**
     * Number of tasks in the queue
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
