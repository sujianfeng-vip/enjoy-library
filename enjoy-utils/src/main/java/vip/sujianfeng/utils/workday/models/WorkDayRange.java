package vip.sujianfeng.utils.workday.models;

/**
 * 工作时间范围
 * @Author SuJianFeng
 * @Date 2023/3/30
 * @Description
 **/
public class WorkDayRange {
    /**
     * 正常工作日（通常为周一到周五，注意补班的时候非周一到周五）
     */
    private boolean containWeekDay = true;
    /**
     * 周六
     */
    private boolean containSaturday = false;
    /**
     * 周日
     */
    private boolean containSunday = false;
    /**
     * 节假日
     */
    private boolean containHoliday = false;

    public boolean isContainWeekDay() {
        return containWeekDay;
    }

    public void setContainWeekDay(boolean containWeekDay) {
        this.containWeekDay = containWeekDay;
    }

    public boolean isContainSaturday() {
        return containSaturday;
    }

    public void setContainSaturday(boolean containSaturday) {
        this.containSaturday = containSaturday;
    }

    public boolean isContainSunday() {
        return containSunday;
    }

    public void setContainSunday(boolean containSunday) {
        this.containSunday = containSunday;
    }

    public boolean isContainHoliday() {
        return containHoliday;
    }

    public void setContainHoliday(boolean containHoliday) {
        this.containHoliday = containHoliday;
    }
}
