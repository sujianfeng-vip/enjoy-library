package vip.sujianfeng.utils.workday.models;

public enum WorkDayType {
    /**
     * 工作日（通常为周一到周五，注意补班的时候非周一到周五）
     */
    WorkDay,

    /**
     * 节假日
     */
    Holiday,

    /**
     * 周六
     */
    Saturday,

    /**
     * 周日
     */
    Sunday
}
