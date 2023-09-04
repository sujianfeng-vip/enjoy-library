package vip.sujianfeng.utils.workday.worktime;

import com.alibaba.fastjson.JSON;
import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.DateTimeUtils;
import vip.sujianfeng.utils.workday.models.WorkDayDefine;
import vip.sujianfeng.utils.workday.models.WorkDayType;
import vip.sujianfeng.utils.workday.models.WorkYearDefine;

import java.util.Date;
import java.util.List;

/**
 * 假日工具类
 */
public class WorkDayHandle {


    public static void main(String[] args) {
        WorkDayHandle workDayDefine = new WorkDayHandle(ConfigUtils.loadResFile("/workday/workday-define.json"));
        int date = 20220101;
        while (date <= 20221231){
            System.out.printf("[%s=%s] ", date, workDayDefine.getType(date).name());
            date = DateTimeUtils.getDateRollDay(date, 1);
        }
    }

    private WorkDayDefine workDayDefine = null;

    public WorkDayHandle(String workDefinJson) {
        this.workDayDefine = JSON.parseObject(workDefinJson, WorkDayDefine.class);
    }

    public WorkDayType getType(Date date){
        return getType(DateTimeUtils.date2int(date));
    }

    public WorkDayType getType(int date){
        List<WorkYearDefine> define = workDayDefine.getDefine();
        for (WorkYearDefine workYearDefine : define) {
            List<Integer> dayList = null;
            if (workYearDefine.getYear() == date / 10000){
                switch (date / 100 % 100){
                    case 1:
                        dayList = workYearDefine.getMonths().getM01();
                        break;
                    case 2:
                        dayList = workYearDefine.getMonths().getM02();
                        break;
                    case 3:
                        dayList = workYearDefine.getMonths().getM03();
                        break;
                    case 4:
                        dayList = workYearDefine.getMonths().getM04();
                        break;
                    case 5:
                        dayList = workYearDefine.getMonths().getM05();
                        break;
                    case 6:
                        dayList = workYearDefine.getMonths().getM06();
                        break;
                    case 7:
                        dayList = workYearDefine.getMonths().getM07();
                        break;
                    case 8:
                        dayList = workYearDefine.getMonths().getM08();
                        break;
                    case 9:
                        dayList = workYearDefine.getMonths().getM09();
                        break;
                    case 10:
                        dayList = workYearDefine.getMonths().getM10();
                        break;
                    case 11:
                        dayList = workYearDefine.getMonths().getM11();
                        break;
                    case 12:
                        dayList = workYearDefine.getMonths().getM12();
                        break;
                }
                if (dayList != null){
                    for (Integer day : dayList) {
                        if (Math.abs(day) == date % 100){
                            return day > 0 ? WorkDayType.WorkDay : WorkDayType.Holiday;
                        }
                    }

                }
            }
        }
        int weekOfDate = DateTimeUtils.getWeekOfDate(DateTimeUtils.int2date(date));
        if (weekOfDate == 0) {
            return WorkDayType.Sunday;
        }
        if (weekOfDate == 6) {
            return WorkDayType.Saturday;
        }
        return WorkDayType.WorkDay;
    }

    public WorkDayDefine getWorkDayDefine() {
        return workDayDefine;
    }
}
