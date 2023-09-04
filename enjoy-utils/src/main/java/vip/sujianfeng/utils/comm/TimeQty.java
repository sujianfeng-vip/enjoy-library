package vip.sujianfeng.utils.comm;

/**
 * 时间数量
 * Created by pilot on 2017/9/10.
 */
public class TimeQty {

    public static int DAY_SECONDS = 3600 * 24;
    public static int HOUR_SECONDS = 3600;
    public static int MINU_SECONDS = 60;
    public static long MAX_LONG = (long) (Math.pow(2, 64) - 1);

    private long days;
    private long hours;
    private long minus;
    private long seconds;

    public TimeQty(long days, long hours, long minus, long seconds){
        this.days = days;
        this.hours = hours;
        this.minus = minus;
        this.seconds = seconds;
    }

    public String cnShow(){
        if (days == 0){
            return String.format("%s时%s分%s秒", hours, minus, seconds);
        }
        return String.format("%s天%s时%s分%s秒", days, hours, minus, seconds);
    }

    public static TimeQty second2timeQty(long totalTime) {
        long days = totalTime / DAY_SECONDS;
        totalTime = totalTime % DAY_SECONDS;
        long hours = totalTime / HOUR_SECONDS;
        totalTime = totalTime % HOUR_SECONDS;
        long minutes = totalTime / MINU_SECONDS;
        long seconds = totalTime % MINU_SECONDS;
        return new TimeQty(days, hours, minutes, seconds);
    }

    public static void main(String[] args){
        System.out.println(second2timeQty(DAY_SECONDS * 24 + 12).cnShow());
        System.out.println(second2timeQty(DAY_SECONDS * 1 + HOUR_SECONDS * 2 + MINU_SECONDS * 3 + 5).cnShow());
        System.out.println((second2timeQty(MAX_LONG).cnShow()));
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinus() {
        return minus;
    }

    public void setMinus(long minus) {
        this.minus = minus;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
