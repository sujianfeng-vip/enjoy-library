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
 * 工作时间计算器
 * 作者时间：sujianfeng / 2021-12-31
 * 功能简述：用于计算两个时间的间隔（只包含工作时间，不包含休假时间）
 * 处理逻辑：将所有工作日的开始工作和结束工作时间点全部纳入列表中，然后遍历该列表进行计算
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
     * 时间区间测试
     * @param workTimeCalc
     * @param begin
     * @param end
     * @param expectation
     */
    private static void testDiff(WorkTimeCalc workTimeCalc, String begin, String end, long expectation){
        long tmp = workTimeCalc.calcDiff(begin, end);
        //Asserts.check(expectation == tmp, String.format("%s ~ %s -> 正确应该为%s秒，实际测试为%s秒", begin, end, expectation, tmp));
        long d = tmp / (24 * 3600);
        long h = tmp % (24 * 3600) / 3600;
        long m = tmp % 3600 / 60;
        long s = tmp % 60;
        logger.info("[{}] {} ~ {} -> {}天{}时{}分{}秒", DateTimeUtils.getDateTimeShow(), begin, end, d, h, m, s);
    }
    private static void testIsWorkTime(WorkTimeCalc workTimeCalc, String dateTime){
        logger.info("{} 为 {} \n ", dateTime, workTimeCalc.isWorkTime(DateTimeUtils.show2dateTime(dateTime).getTime() / 1000) ? "工作时间" : "休息时间");
    }

    /**
     * 时间偏移测试
     */
    private static void testShiftingBlock(WorkTimeCalc workTimeCalc, String time, long shifting, String expectation) {
        long tmp = workTimeCalc.shiftingBlock(time, shifting);
        String tmpTime = DateTimeUtils.timestamp2String(tmp * 1000);
        //Asserts.check(expectation.equals(tmpTime), String.format("%s偏移%s秒后，结果应该为%s，实际测试为%s", time, shifting, expectation, tmpTime));
        logger.info("[{}] {} 偏移{}秒后，结果为{} \n", DateTimeUtils.getDateTimeShow(), time, shifting, tmpTime);
    }

    /**
     * 工作日定义数据
     */
    private WorkDayHandle workDayDefine;
    /**
     * 时间点列表
     */
    private List<WorkTimePoint> list;
    /**
     * 工作时间类型：0-工作日，1-每天
     */
    private int workTimeType;
    /**
     * 工作日开始时间（例如：830）
     */
    private int workDayBegin;
    /**
     * 工作日结束时间（例如：1800）
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
     * 初始化所有工作时间点列表（上班、下班时间）
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

    /**
     * 计算两个时间间隔（只包含工作时间）
     * 原理是，将合理区间内地所有工作区间和计算区间进行交集计算即可
     * @param beginTime
     * @param endTime
     * @return
     */
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

    /**
     * 判断是否为工作时间
     * @param time
     * @return
     */
    public boolean isWorkTime(long time){
        WorkTimePoint lastPoint = null;
        for (WorkTimePoint point : this.list) {
            if (lastPoint != null) {
                //刚好处于上下班时间点，那么判断为上班
                if (lastPoint.getTime() == time || point.getTime() == time){
                    return true;
                }
                //在上班和下班区间范围内
                if (lastPoint.getWorkTimePointType() == WorkTimePointType.OnWork && time > lastPoint.getTime() && time < point.getTime()){
                    return true;
                }
            }
            lastPoint = point;
        }
        return false;
    }

    /**
     * 计算两组区间之间的交集长度
     * 注意提前条件: b1 < e1， b2 < e2
     * @return
     */
    private long calcIntersect(long b1, long e1, long b2, long e2) {
        if (b1 > e2 || b2 > e1){
            return 0;
        }
        //交集1
        if (b2 < b1 && e2 < e1){
            return e2 - b1;
        }
        //交集2
        if (b1 < b2 && e1 < e2){
            return e1 - b2;
        }
        //1包含2
        if (b1 < b2 && e2 < e1){
            return e2 - b2;
        }
        //2包含1
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

    /**
     * 返回指定时间点的偏移几秒
     * @param time
     */
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
        //在工作区间，要判断剩余工作时间是否足够偏移，不够的部分还需要继续偏移
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
