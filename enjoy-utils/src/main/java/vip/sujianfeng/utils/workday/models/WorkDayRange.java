package vip.sujianfeng.utils.workday.models;

/**
 * Working time range
 * author SuJianFeng
 * createTime  2023/3/30
 * description
 **/
public class WorkDayRange {
    /**
     * Normal working days (usually from Monday to Friday, please note that the make-up shift is not from Monday to Friday)
     */
    private boolean containWeekDay = true;
    /**
     * Saturday
     */
    private boolean containSaturday = false;
    /**
     * Sunday
     */
    private boolean containSunday = false;
    /**
     * festival and holiday
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
