package vip.sujianfeng.utils;

import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.DateTimeUtils;
import vip.sujianfeng.utils.workday.models.WorkDayRange;
import vip.sujianfeng.utils.workday.utils.WorkTimeUtils;
import vip.sujianfeng.utils.workday.worktime.WorkDayHandle;

/**
 * author SuJianFeng
 * createTime  2023/1/17
 * Description
 **/
public class WorkTimeUtilsTest {

    public static void main(String[] args) {
        WorkDayHandle workDayHandle = new WorkDayHandle(ConfigUtils.loadResFile("/workday/workday-define.json"));

        WorkDayRange workDayRange = new WorkDayRange();
        workDayRange.setContainWeekDay(true);
        workDayRange.setContainHoliday(false);
        workDayRange.setContainSaturday(true);
        workDayRange.setContainSunday(false);

        long endTime = DateTimeUtils.int2date(20230624).getTime() / 1000;
        endTime = WorkTimeUtils.gotoWorkTime(workDayHandle, endTime, true, 0, 2400, workDayRange);
        long beginTime = WorkTimeUtils.offsetTime(workDayHandle, endTime, - 3600 * 24L, 0, 2400, workDayRange);

        System.out.println("开始时间 => " + DateTimeUtils.timestamp2String(beginTime * 1000));
        System.out.println("结束时间 => " + DateTimeUtils.timestamp2String(endTime * 1000));
    }
}
