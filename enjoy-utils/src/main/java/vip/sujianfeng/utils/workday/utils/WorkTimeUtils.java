package vip.sujianfeng.utils.workday.utils;


import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.DateTimeUtils;
import vip.sujianfeng.utils.workday.models.WorkDayRange;
import vip.sujianfeng.utils.workday.models.WorkDayType;
import vip.sujianfeng.utils.workday.worktime.WorkDayHandle;

import java.text.ParseException;
import java.util.Date;

/**
 * 计算两个时间间隔（排除节假日，只包含工作时间）
 * 这种方法比较简单暴力，但效率差
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
            System.out.printf("%s到%s间隔%s秒", begin, end, interval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算两个工作时间直接的间隔（排除休息时间）
     * @param workDayHandle
     * @param beginTime 开始时间（时间戳-秒）
     * @param endTime 结束时间（时间戳-秒）
     * @param workDayBegin 工作日开始时间， 例如 800
     * @param workDayEnd 工作日结束时间，例如 1800
     * @param workDayRange 工作时间范围
     * @return
     */
    public static long getInterval(WorkDayHandle workDayHandle, long beginTime, long endTime, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        if (workDayBegin == 0 && workDayEnd == 0){
            return endTime - beginTime;
        }
        int step = 60;
        long result = 0;
        while (beginTime <= endTime){
            beginTime += step; //每1分钟判断一次
            boolean workTime = isWorkTime(workDayHandle, beginTime, workDayRange, workDayBegin, workDayEnd); //工作日
            //工作日
            if (workTime) {
                result += step;
            }
        }
        return result;
    }


    /**
     * 判断指定时间是否为工作时间段
     * @param time 时间戳到秒
     * @param workDayBegin 工作日开始时间（分）
     * @param workDayEnd 工作日结束时间（分）
     * @return
     */
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

    /**
     * 工作日时间偏移计算函数
     * 例如：8:00偏移10秒为8:10
     * @param workDayHandle
     * @param time 指定时间
     * @param offset 偏移时间
     * @param workDayBegin 工作日开始时间， 例如 800
     * @param workDayEnd 工作日结束时间，例如 1800
     * @param workDayRange 工作时间范围
     * @return
     */
    public static long offsetTime(WorkDayHandle workDayHandle, long time, long offset, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        if (workDayBegin == 0 && workDayEnd == 0){
            return time + offset;
        }
        int sign = offset > 0 ? 1 : -1;
        int stepSeconds = 60;
        while (offset * sign > 0){
            time += stepSeconds * sign;
            //System.out.println("当前时间 => " + DateTimeUtils.timestamp2String(time * 1000));
            boolean workTime = isWorkTime(workDayHandle, time, workDayRange, workDayBegin, workDayEnd); //是否为工作时间
            //System.out.println("当前时间 => " + DateTimeUtils.timestamp2String(time * 1000) + " => 是否工作时间 = " + workTime);
            //工作时间
            if (workTime) {
                offset -= stepSeconds * sign;
            }
        }
        return time;
    }

    /**
     * 跳到工作时间
     * @param workDayHandle
     * @param time
     * @param back  true: 往前跳，false: 往后跳
     * @param workDayBegin
     * @param workDayEnd
     * @param workDayRange
     * @return
     */
    public static long gotoWorkTime(WorkDayHandle workDayHandle, long time, Boolean back, int workDayBegin, int workDayEnd, WorkDayRange workDayRange){
        while (!isWorkTime(workDayHandle, time, workDayRange, workDayBegin, workDayEnd)) {
            time = offsetTime(workDayHandle, time, back ? -1 : 1, workDayBegin, workDayEnd, workDayRange);
        }
        return time;
    }
}
