package vip.sujianfeng.utils.workday.models;

public class WorkTimePoint {
    /**
     * Time point (timestamp seconds)
     */
    private long time;

    /**
     * Time point type
     */
    private WorkTimePointType workTimePointType;

    public WorkTimePoint(long time, WorkTimePointType workTimePointType) {
        this.time = time;
        this.workTimePointType = workTimePointType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public WorkTimePointType getWorkTimePointType() {
        return workTimePointType;
    }

    public void setWorkTimePointType(WorkTimePointType workTimePointType) {
        this.workTimePointType = workTimePointType;
    }
}
