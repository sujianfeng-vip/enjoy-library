package vip.sujianfeng.utils.workday.utils;


import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.DateTimeUtils;
import vip.sujianfeng.utils.workday.models.WorkDayRange;
import vip.sujianfeng.utils.workday.models.WorkDayType;
import vip.sujianfeng.utils.workday.worktime.WorkDayHandle;

import java.text.ParseException;
import java.util.Date;

/**
 *Calculate two time intervals (excluding holidays and only including working hours)
 *This method is relatively simple and violent, but inefficient
 */
public class WorkTimeUtils {

    public static void main(String[] args) {
        try {
            WorkDayHandle workDayHandle = new WorkDayHandle(ConfigUtils.loadResFile("/workday/workday-define.json"));
            String begin = "2021-12-01";
            String end = "2021-12-11";
            Date date1 = DateTimeUtils.show2date(begin);
            Date date2 = DateTimeUtils.show2date(end);

            long interval = getInterval(workDayHandle, date1.getTime()/1000, date2.getTime()/1000, 800, 1800, new WorkDayRange());
            System.out.printf("%s to %s => %s seconds", begin, end, interval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static long getInterval(WorkDayHandle workDayHandle, long beginTime, long endTime, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        if (workDayBegin == 0 && workDayEnd == 0){
            return endTime - beginTime;
        }
        int step = 60;
        long result = 0;
        while (beginTime <= endTime){
            beginTime += step; //Judge every 1 minute
            boolean workTime = isWorkTime(workDayHandle, beginTime, workDayRange, workDayBegin, workDayEnd); //weekday
            //weekday
            if (workTime) {
                result += step;
            }
        }
        return result;
    }

    public static boolean isWorkTime(WorkDayHandle workDayHandle, long time, WorkDayRange workDayRange, int workDayBegin, int workDayEnd){
        WorkDayType workType = getWorkType(workDayHandle, time);
        if (workType == WorkDayType.WorkDay) {
            if (workDayRange.isContainWeekDay()){
                Date date = new Date(time * 1000L);
                int iTime = DateTimeUtils.time2Int(date)/100; //单位到分
                return iTime >= workDayBegin && iTime <= workDayEnd;
            }
            return false;
        }
        if (workType == WorkDayType.Holiday) {
            return workDayRange.isContainHoliday();
        }
        if (workType == WorkDayType.Saturday) {
            return workDayRange.isContainSaturday();
        }
        if (workType == WorkDayType.Sunday) {
            return workDayRange.isContainSunday();
        }
        return false;
    }

    public static WorkDayType getWorkType(WorkDayHandle workDayHandle, long time){
        Date date = new Date(time * 1000L);
        int iDate = DateTimeUtils.date2int(date);
        return workDayHandle.getType(iDate);
    }

    public static long offsetTime(WorkDayHandle workDayHandle, long time, long offset, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        if (workDayBegin == 0 && workDayEnd == 0){
            return time + offset;
        }
        int sign = offset > 0 ? 1 : -1;
        int stepSeconds = 60;
        while (offset * sign > 0){
            time += stepSeconds * sign;
            boolean workTime = isWorkTime(workDayHandle, time, workDayRange, workDayBegin, workDayEnd);
            if (workTime) {
                offset -= stepSeconds * sign;
            }
        }
        return time;
    }

    public static long gotoWorkTime(WorkDayHandle workDayHandle, long time, Boolean back, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        while (!isWorkTime(workDayHandle, time, workDayRange, workDayBegin, workDayEnd)) {
            time = offsetTime(workDayHandle, time, back ? -1 : 1, workDayBegin, workDayEnd, workDayRange);
        }
        return time;
    }
}
