package vip.sujianfeng.utils.workday.worktime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.DateTimeUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.workday.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *Working time calculator
 *Author's Date: Sujianfeng/December 31, 2021
 *Function description: used to calculate the interval between two times (only including working hours, not vacation time)
 *Processing logic: Include all start and end work time points of all working days in the list, and then traverse the list for calculation
 *
 */
public class WorkTimeCalc {

    private static Logger logger = LoggerFactory.getLogger(WorkTimeCalc.class);

    public static void main(String[] args) {
        WorkDayHandle workDayDefine = new WorkDayHandle(ConfigUtils.loadResFile("/workday/workday-define.json"));
        WorkTimeCalc workTimeCalc = new WorkTimeCalc(workDayDefine, 0, 830, 1800);
//        testDiff(workTimeCalc, "2021-12-01 07:30:00", "2022-01-01 10:00:00", 786600);
//        testDiff(workTimeCalc, "2021-01-01 07:30:00", "2022-01-01 10:00:00", 8550000);
//        testDiff(workTimeCalc, "2021-12-31 09:30:00", "2021-12-31 10:00:00", 1800);
//        testDiff(workTimeCalc, "2021-12-31 09:30:00", "2022-01-01 10:00:00", 30600);
//        testDiff(workTimeCalc, "2021-12-31 07:30:00", "2022-01-01 10:00:00", 34200);
//
//        testShiftingBlock(workTimeCalc, "2021-12-31 08:40:00", -30 * 60, "2021-12-30 17:40:00");
//        testShiftingBlock(workTimeCalc, "2021-12-31 07:30:00", 30 * 60, "2021-12-31 09:00:00");
//        testShiftingBlock(workTimeCalc, "2021-12-31 07:30:00", - 30 * 60, "2021-12-30 17:30:00");
//        testShiftingBlock(workTimeCalc, "2021-12-31 17:50:00", 30 * 60, "2022-01-04 08:50:00");

        testIsWorkTime(workTimeCalc, "2021-12-01 07:30:00");
        testIsWorkTime(workTimeCalc, "2021-12-01 08:30:00");
        testIsWorkTime(workTimeCalc, "2021-12-01 13:30:00");
        testIsWorkTime(workTimeCalc, "2021-12-01 18:00:00");
        testIsWorkTime(workTimeCalc, "2021-12-01 18:00:01");
    }

    /**
     * Time interval testing
     * @param workTimeCalc
     * @param begin
     * @param end
     * @param expectation
     */
    private static void testDiff(WorkTimeCalc workTimeCalc, String begin, String end, long expectation){
        long tmp = workTimeCalc.calcDiff(begin, end);
        long d = tmp / (24 * 3600);
        long h = tmp % (24 * 3600) / 3600;
        long m = tmp % 3600 / 60;
        long s = tmp % 60;
        logger.info("[{}] {} ~ {} -> {}days {}hour{}minutes {}seconds", DateTimeUtils.getDateTimeShow(), begin, end, d, h, m, s);
    }
    private static void testIsWorkTime(WorkTimeCalc workTimeCalc, String dateTime){
        logger.info("{} is {} \n ", dateTime, workTimeCalc.isWorkTime(DateTimeUtils.show2dateTime(dateTime).getTime() / 1000) ? "working hours" : "break");
    }

    /**
     * Time offset test
     */
    private static void testShiftingBlock(WorkTimeCalc workTimeCalc, String time, long shifting, String expectation) {
        long tmp = workTimeCalc.shiftingBlock(time, shifting);
        String tmpTime = DateTimeUtils.timestamp2String(tmp * 1000);
        //Asserts.check(expectation.equals(tmpTime), String.format("%s偏移%s秒后，结果应该为%s，实际测试为%s", time, shifting, expectation, tmpTime));
        logger.info("[{}] {} After offsetting {} seconds, the result is {} \n", DateTimeUtils.getDateTimeShow(), time, shifting, tmpTime);
    }

    /**
     * Weekday Definition Data
     */
    private WorkDayHandle workDayDefine;
    /**
     * Time point list
     */
    private List<WorkTimePoint> list;
    /**
     * Working time type: 0-working days, 1-daily
     */
    private int workTimeType;
    /**
     * Starting time of working day (e.g. 830)
     */
    private int workDayBegin;
    /**
     * End of workday (e.g. 1800)
     */
    private int workDayEnd;

    public WorkTimeCalc(WorkDayHandle workDayDefine, int workTimeType, int workDayBegin, int workDayEnd) {
        this.workDayDefine = workDayDefine;
        this.workTimeType = workTimeType;
        if (workDayBegin == 0 && workDayEnd == 0){
            this.workDayBegin = 830;
            this.workDayEnd = 1800;
        }else{
            this.workDayBegin = workDayBegin;
            this.workDayEnd = workDayEnd;
        }
        this.initWorkTimePointList();
    }

    /**
     * Initialize a list of all work time points (start and end times)
     */
    private void initWorkTimePointList(){
        this.list = new ArrayList<>();
        WorkDayDefine workDayDefine = this.workDayDefine.getWorkDayDefine();
        for (WorkYearDefine workYear : workDayDefine.getDefine()) {
            int date = workYear.getYear() * 10000 + 101;
            while (date / 10000 == workYear.getYear()){
                if (this.workTimeType == 1 || this.workDayDefine.getType(date) == WorkDayType.WorkDay){
                    //上班
                    this.addTimePoint(date, this.workDayBegin, WorkTimePointType.OnWork);
                    //下班
                    this.addTimePoint(date, this.workDayEnd, WorkTimePointType.OffWork);
                }
                date = DateTimeUtils.getDateRollDay(date, 1);
            }
        }
    }

    public long calcDiff(String beginTime, String endTime){
        return calcDiff(DateTimeUtils.show2dateTime(beginTime), DateTimeUtils.show2dateTime(endTime));
    }

    public long calcDiff(Date beginTime, Date endTime){
        return calcDiff(beginTime.getTime() / 1000, endTime.getTime() / 1000);
    }

    public long calcDiff(long beginTime, long endTime){
        if (beginTime > endTime){
            long tmp = beginTime;
            beginTime = endTime;
            endTime = tmp;
        }
        long result = 0;
        WorkTimePoint onWork = null;
        for (WorkTimePoint point : this.list) {
            if (point.getWorkTimePointType() == WorkTimePointType.OffWork){
                if (onWork != null){
                    result += calcIntersect(beginTime, endTime, onWork.getTime(), point.getTime());
                }
            } else {
                onWork = point;
            }
        }
        return result;
    }

    public boolean isWorkTime(long time){
        WorkTimePoint lastPoint = null;
        for (WorkTimePoint point : this.list) {
            if (lastPoint != null) {
                //If it happens to be at the time of commuting, then it is judged as working
                if (lastPoint.getTime() == time || point.getTime() == time){
                    return true;
                }
                //Within the range of work and off duty intervals
                if (lastPoint.getWorkTimePointType() == WorkTimePointType.OnWork && time > lastPoint.getTime() && time < point.getTime()){
                    return true;
                }
            }
            lastPoint = point;
        }
        return false;
    }

    /**
     * Calculate the intersection length between two sets of intervals, paying attention to the advance conditions:
     * b1 < e1 b2 < e2
     * @return
     */
    private long calcIntersect(long b1, long e1, long b2, long e2) {
        if (b1 > e2 || b2 > e1){
            return 0;
        }
        //Intersection 1
        if (b2 < b1 && e2 < e1){
            return e2 - b1;
        }
        //Intersection 2
        if (b1 < b2 && e1 < e2){
            return e1 - b2;
        }
        //1 includes 2
        if (b1 < b2 && e2 < e1){
            return e2 - b2;
        }
        //2 includes 1
        if (b2 < b1 && e1 < e2){
            return e1 - b1;
        }
        return 0;
    }

    public long shiftingBlock(String dateTime, long shifting){
        return shiftingBlock(DateTimeUtils.show2dateTime(dateTime), shifting);
    }

    public long shiftingBlock(Date dateTime, long shifting){
        return shiftingBlock(dateTime.getTime() / 1000, shifting);
    }


    private void addTimePoint(int date, int time, WorkTimePointType workTimePointType){
        String datePoint = DateTimeUtils.date2show(date) + String.format(" %s:%s:%s", StringUtilsEx.f2(time / 100), StringUtilsEx.f2(time % 100), "00");
        long timePoint = DateTimeUtils.show2dateTime(datePoint).getTime() / 1000;
        this.list.add(new WorkTimePoint(timePoint, workTimePointType));
    }

    public long shiftingBlock(long time, long diff){
        WorkTimePoint begin = null;
        WorkTimePoint end = null;
        for (int i = 0; i < this.list.size() - 1; i++) {
            begin = this.list.get(i);
            end = this.list.get(i + 1);
            if (time >= begin.getTime() && time < end.getTime()){ //在区间内
                if (begin.getWorkTimePointType() == WorkTimePointType.OffWork){
                    //在休假区间内
                    return diff > 0 ? end.getTime() + diff : begin.getTime() + diff;
                }
                break;
            }
        }
        if (begin == null || end == null){
            return time + diff;
        }
        //In the work interval, it is necessary to determine whether the remaining working time is sufficiently offset, and the insufficient part needs to be further offset
        if (diff > 0 && time + diff > end.getTime()){
            return shiftingBlock(end.getTime() + 60, time + diff - end.getTime());
        }
        if (diff < 0 && time + diff < begin.getTime()){
            return shiftingBlock(begin.getTime() - 60, time + diff - begin.getTime());
        }
        return time + diff;
    }

    public WorkDayHandle getWorkDay() {
        return workDayDefine;
    }

    public void setWorkDay(WorkDayHandle workDayDefine) {
        this.workDayDefine = workDayDefine;
    }

    public List<WorkTimePoint> getList() {
        return list;
    }

    public void setList(List<WorkTimePoint> list) {
        this.list = list;
    }

    public int getWorkTimeType() {
        return workTimeType;
    }

    public void setWorkTimeType(int workTimeType) {
        this.workTimeType = workTimeType;
    }

    public int getWorkDayBegin() {
        return workDayBegin;
    }

    public void setWorkDayBegin(int workDayBegin) {
        this.workDayBegin = workDayBegin;
    }

    public int getWorkDayEnd() {
        return workDayEnd;
    }

    public void setWorkDayEnd(int workDayEnd) {
        this.workDayEnd = workDayEnd;
    }
}
