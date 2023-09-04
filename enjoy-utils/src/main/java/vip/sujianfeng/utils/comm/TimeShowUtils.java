package vip.sujianfeng.utils.comm;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by zhangwenchun on 2016/8/16.
 */
public class TimeShowUtils {
    public static String timeShow(String date) {
        String pastTime = "";
        if (StringUtilsEx.isNotEmpty(date)) {
            Date beginDate = DateTimeUtils.show2dateTime(date);
            Date endDate = new Date();
            long diff = endDate.getTime() - beginDate.getTime();
            if (diff > 1000 * 3600 * 24) {
                long day = diff / (1000 * 3600 * 24);
                pastTime = day + "天前";
            } else {
                if (diff > 1000 * 3600) {
                    long hours = diff / (1000 * 3600);
                    pastTime = hours + "小时前";
                } else {
                    if (diff > 1000 * 60) {
                        long m = diff / (1000 * 60);
                        pastTime = m + "分钟前";
                    } else {
                        pastTime = "刚刚";
                    }
                }

            }
        }
        return pastTime;
    }

    public static String timeLeft(String date) throws ParseException {
        String timeLeft = "";
        if (StringUtilsEx.isNotEmpty(date)) {
            Date now_date = new Date();
            Date date_after = DateTimeUtils.str2DateTime(date);
            long diff = (date_after.getTime() - now_date.getTime()) / 1000; // 大于当前时间
            System.out.println("直播预告时间：" + date_after.getTime());
            System.out.println("当前时间：" + now_date.getTime());
            if (diff > 0) {
                if (diff > 3600 * 24) {
                    long day = diff / (3600 * 24);
                    timeLeft = day + "天";
                } else {
                    if (diff > 3600) {
                        long hours = diff / 3600;
                        timeLeft = hours + "小时";
                    } else {
                        if (diff > 60) {
                            long m = diff / 60;
                            timeLeft = m + "分钟";
                        } else {
                            timeLeft = diff + "秒";
                        }
                    }

                }
            }
        }
        return timeLeft;
    }

    public static String getDurationStr(int duration_int) throws ParseException {
        String duration = "";
        if (duration_int > 0) {
//            if (duration_int > 3600 * 24) {
//                int day = duration_int / 3600 * 24;
//                duration = day + "天";
//
//                duration_int = duration_int - day * 3600 * 24;
//            }
//            if (duration_int > 3600) {
//                int hours = duration_int / 3600;
//                duration = duration + hours + "小时";
//                duration_int = duration_int - hours * 3600;
//            }
            if (duration_int > 60) {
                int m = duration_int / 60;
                duration = m + ":";
                duration_int = duration_int - m * 60;
            }
            duration = duration + duration_int;
        }
        return duration;
    }
}
