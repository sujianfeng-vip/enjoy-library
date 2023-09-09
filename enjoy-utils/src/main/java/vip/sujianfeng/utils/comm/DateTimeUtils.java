package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Time Processing Tool Class
 * author sujianfeng
 *
 */
public class DateTimeUtils {
	private static Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
    /**
     * Format 2-bit integer: 1 -> 01,  11 - > 11
     * @param i
     * @return
     */
    private static String f2(int i){
    	return i > 9 ? String.valueOf(i) : "0" + String.valueOf(i);
    }

	public static int date2int(Date date)
    {
    	if (date == null) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int time2Int(Date time)
    {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return cal.get(Calendar.HOUR_OF_DAY) * 10000 + cal.get(Calendar.MINUTE) * 100 + cal.get(Calendar.SECOND);
    }

    public static Date int2date(int year, int month, int day){
    	DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String y = String.valueOf(year);
    	String m = String.valueOf(month);
    	if (month < 10)
    		m = "0" + m;
    	String d = String.valueOf(day);
    	if (day < 10)
    		d = "0" + d;
    	String sDate = String.format("%s-%s-%s 00:00:00", y, m, d);
		Date date = null;
		try {
			date = sdf.parse(sDate);
		} catch (ParseException e) {
			logger.error(e.toString(), e);
		}
		return date;
    }

    public static Date int2date(int date){
    	int y = date / 10000;
    	int m = date / 100 % 100;
    	int d = date % 100;
    	return int2date(y, m, d);
    }

    public static String long2show(Long longdt){
		int intDate = (int) (longdt / 1000000);
		int intTime = (int) (longdt % 1000000);
		int y = intDate / 10000;
		int m = intDate / 100 % 100;
		int d = intDate % 100;
		int hh = intTime / 10000;
		int mm = intTime / 100 % 100;
		int ss = intTime % 100;
		return String.format("%s-%s-%s %s:%s:%s", y, f2(m),f2(d), f2(hh), f2(mm), f2(ss));
    }

    public static String int2show(int intdate){
		int y = intdate / 10000;
		int m = intdate / 100 % 100;
		int d = intdate % 100;
		return String.format("%s-%s-%s", y, f2(m),f2(d));
    }

    public static Date int2dateTime(int year, int month, int day, int hour, int min, int sec) throws ParseException{
    	DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String y = String.valueOf(year);
    	String m = String.valueOf(month);
    	if (month < 10)
    		m = "0" + m;
    	String d = String.valueOf(day);
    	if (day < 10)
    		d = "0" + d;

    	String hh = String.valueOf(hour);
    	if (hour < 10)
    		hh = "0" + hh;

    	String mm = String.valueOf(min);
    	if (min < 10)
    		mm = "0" + mm;

    	String ss = String.valueOf(sec);
    	if (sec < 10)
    		ss = "0" + ss;

    	String sDateTime = String.format("%s-%s-%s %s:%s:%s", y, m, d, hh, mm, ss);
    	Date datetime = sdf.parse(sDateTime);
    	return datetime;
    }

    public static Date show2dateTime(String date){
    	DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = date.replace("T", " ");
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error(e.toString(), e);
			return new Date();
		}
	}

    public static Date show2date(String date) throws ParseException{
    	DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.parse(date);
    }
    /*字符转日期，到分*/
	public static Date str2DateTime(String date) throws ParseException{
		DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.parse(date);
	}


	public static int show2Int(String dateStr) throws ParseException{
    	if (StringUtilsEx.isEmpty(dateStr))
    		return 0;
    	Date date = show2date(dateStr);
    	return date2int(date);
    }

    public static int addYearMonth(int yearMonth, int diffMonthCount)
    {
    	int year = yearMonth / 100;
    	int month = yearMonth % 100;
    	return addYearMonth(year, month);
    }

    public static int addYearMonth(int year, int month, int diffMonthCount)
    {
    	month += diffMonthCount;
    	while (true)
    	{
    		if (month >= 1 && month <= 12)
    		{
    			break;
    		}
    		if (month < 1)
			{
				month += 12;
				year--;
			}
			else
			{
				month -= 12;
				year++;
			}
    	}
    	return year * 100 + month;
    }

	public static String date2SqlItem(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String result = date2SqlItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
		return result;
	}

	public static String date2SqlItem(int year, int month, int day)
	{
		int iDate = year * 10000 + month * 100 + day;
		String result = String.format("Convert(DateTime, '%s', 112)", String.valueOf(iDate));
		return result;
	}

	public static String datetime2longShow(Date date){
		return datetime2longShow(date, true, true, true, true);
	}

	public static String datetime2longShow(Date date, boolean haveHour, boolean haveMinute, boolean haveSecond, boolean haveMilliSecond){
		if (date == null){
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);

		StringBuilder sb = new StringBuilder();
		sb.append(y);
		sb.append("-");
		if (m < 10)
			sb.append("0");
		sb.append(m);
		sb.append("-");
		if (d < 10)
			sb.append("0");
		sb.append(d);
		sb.append(" ");

		if (haveHour){
			int hh =  cal.get(Calendar.HOUR_OF_DAY);
			if (hh < 10)
				sb.append("0");
			sb.append(hh);
		}
		if (haveMinute){
			int mm =  cal.get(Calendar.MINUTE);
			sb.append(":");
			if (mm < 10)
				sb.append("0");
			sb.append(mm);
		}
		if (haveSecond){
			int sc =  cal.get(Calendar.SECOND);
			sb.append(":");
			if (sc < 10)
				sb.append("0");
			sb.append(sc);
		}
		if (haveMilliSecond){
			int ms =  cal.get(Calendar.MILLISECOND);
			sb.append(" ");
			sb.append(ms);
		}

		return sb.toString();
	}

	public static Long datetime14long(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int hh =  cal.get(Calendar.HOUR_OF_DAY);
		int mm =  cal.get(Calendar.MINUTE);
		int sc =  cal.get(Calendar.SECOND);

		StringBuilder sb = new StringBuilder();
		sb.append(y);
		if (m < 10)
			sb.append("0");
		sb.append(m);
		if (d < 10)
			sb.append("0");
		sb.append(d);
		if (hh < 10)
			sb.append("0");
		sb.append(hh);
		if (mm < 10)
			sb.append("0");
		sb.append(mm);
		if (sc < 10)
			sb.append("0");
		sb.append(sc);

		return Long.valueOf(sb.toString());
	}


	public static String getNow(){
		return datetime2longShow(new Date());
	}

    public static String getDateTimeShow(){
        return datetime2longShow(new Date(), true, true, true, false);
    }

    public static java.sql.Date getSqlNow(){
        return new java.sql.Date(new Date().getTime());
    }

	public static String getToday(){
		return date2show(new Date());
	}

	public static String getNow(String pattern) {
		if(StringUtilsEx.isEmpty(pattern))
			pattern = "yyyy-MM-dd";
		DateFormat sdf= new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}

	public static Date str2Date(String dateStr, String pattern) {
		if(StringUtilsEx.isEmpty(dateStr))
			return null;
		DateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch(ParseException e) {
			//logger.error("时间格式转换错误，dateStr: " + dateStr, e);
            logger.error(e.toString(), e);
		}
		return null;
	}

	public static String formatDateStr(String dateStr, String srcPattern,
			String destPattern) {
		Date date = str2Date(dateStr, srcPattern);
		if(null != date)
			return new SimpleDateFormat(destPattern).format(date);
		else
			return dateStr;
	}

	public static int today2int(){
		return date2int(new Date());
	}

	public static Long getDiffDays(int beginDate, int endDate){
		Date bDate = int2date(beginDate);
		Date eDate = int2date(endDate);
		return getDiffDays(bDate, eDate);
	}

	public static Long getDiffDays(Date beginDate, Date endDate){
		if (beginDate == null || beginDate == null) return Long.valueOf(0);
		Long diff = endDate.getTime() - beginDate.getTime();
		return diff/1000/60/60/24;
	}

	public static Long getDiffSeconds(int beginDate, int endDate){
		Date bDate = int2date(beginDate);
		Date eDate = int2date(endDate);
		Long diff = eDate.getTime() - bDate.getTime();
		return diff/1000;
	}

	public static Long getDiffSeconds(Date beginDate, Date endDate){
		Long diff = endDate.getTime() - beginDate.getTime();
		return diff/1000;
	}

	public static Long getDiffMilliSecond(Date beginDate, Date endDate){
		Long diff = endDate.getTime() - beginDate.getTime();
		return diff;
	}

	public static Date getDateRollDayEx(Date date, int diff){
		if (diff == 0)
			return date;
		Calendar calc = Calendar.getInstance();
		calc.setTime(date);
		//calc.roll(Calendar.DAY_OF_YEAR, diff);
		calc.add(Calendar.DATE, diff);
		return calc.getTime();
	}

	public static int getDateRollDay(Date date, int diff){
		if (diff == 0)
			return date2int(date);
		Calendar calc = Calendar.getInstance();
		calc.setTime(date);
		//calc.roll(Calendar.DAY_OF_YEAR, diff);
		calc.add(Calendar.DATE, diff);
		return date2int(calc.getTime());
	}

	public static int getDateRollWeek(Date date, int diff){
		if (diff == 0)
			return date2int(date);
		Calendar calc = Calendar.getInstance();
		calc.setTime(date);
		calc.roll(Calendar.WEEK_OF_YEAR, diff);
		return date2int(calc.getTime());
	}

	public static int getDateRollMonth(Date date, int diff){
		int idate = date2int(date);
		if (diff == 0)
			return idate;
		int year = idate/10000;
		int month = idate/100%100;
		int d = idate%100;
		int y = (year*12 + month + diff)/12;
		int m = (year*12 + month + diff)%12;
		if (m == 0){
			m = 12;
			y--;
		}
		return y*10000+m*100+d;
	}

	public static int getDateRollDay(int date, int diff){
		return getDateRollDay(int2date(date), diff);
	}

	public static int getDateRollWeek(int date, int diff){
		return getDateRollWeek(int2date(date), diff);
	}

	public static int getDateRollMonth(int date, int diff){
		return getDateRollMonth(int2date(date), diff);
	}

	public static long mergeTime(int date, int time){
		String temp = String.format("%s%s", date, time);
		return ConvertUtils.cLong(temp);
	}

	public static Date getThisMonday() {
		return getThisMonday(null);
	}
	public static Date getThisMonday(Date date) {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		if (date != null){
			cd.setTime(date);
		}
		// Today is the day of the week, Sunday is the first day, Monday is the second day, and Wednesday is the second day
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; //Because Monday is the first day in China, it is reduced by 1 here
		if (dayOfWeek == 0){
			dayOfWeek = 7;
		}
		mondayPlus = 1 - dayOfWeek;
		GregorianCalendar currentDate = new GregorianCalendar();
		if (date != null){
			currentDate.setTime(date);
		}
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		return monday;

	}

	public static int getMonthFirstIntDate(int date){
		int y = date / 10000;
		int m = date / 100 % 100;
		return y * 10000 + m * 100 + 1;
	}

	public static int getMonthFirstIntDate(Date date){
		return getMonthFirstIntDate(date2int(date));
	}

	public static int getThisMonthIntDate(){
		return getMonthFirstIntDate(date2int(new Date()));
	}

	public static Date getMonthFirstDate(int date) throws ParseException{
		int intDate = getMonthFirstIntDate(date);
		return int2date(intDate);
	}

	public static Date getMonthFirstDate(Date date) throws ParseException{
		return getMonthFirstDate(date2int(date));
	}

	public static Date getMonthFirstDate() throws ParseException{
		return getMonthFirstDate(date2int(new Date()));
	}


	public static int getThisYear(){
		int date = date2int(new Date());
		return date / 10000;
	}

	public static int getThisMonth(){
		int date = date2int(new Date());
		return date / 100 % 100;
	}

	public static int getThisDay(){
		int date = date2int(new Date());
		return date % 100;
	}

	public static String date2show(int date){
		return date2show(date, true, true, true);
	}

	public static String date2show(Date date){
		return date2show(date2int(date));
	}

	public static String date2show(int date, boolean showYear, boolean showMonth, boolean showDay){
		StringBuilder sb = new StringBuilder();
		if (showYear){
			sb.append(String.valueOf(date/10000));
		}
		if (showMonth){
			int m = date/100%100;
			String month = String.valueOf(m);
			if (sb.length() > 0)
				sb.append("-");
			if (m < 10)
				sb.append("0");
			sb.append(month);
		}
		if (showDay){
			int d = date%100;
			String day = String.valueOf(d);
			if (sb.length() > 0)
				sb.append("-");
			if (d < 10)
				sb.append("0");
			sb.append(day);
		}
		return sb.toString();
	}

	public static String time2show(int time){
		return time2show(time, true, true, true);
	}

	public static String time2show(int time, boolean showHour, boolean showMinute, boolean showSecond){
		StringBuilder sb = new StringBuilder();
		if (showHour){
			int h = time/10000;
			String month = String.valueOf(h);
			if (h < 10)
				sb.append("0");
			sb.append(month);
		}
		if (showMinute){
			int m = time/100%100;
			String minute = String.valueOf(m);
			if (sb.length() > 0)
				sb.append(":");
			if (m < 10)
				sb.append("0");
			sb.append(minute);
		}
		if (showSecond){
			int s = time%100;
			String second = String.valueOf(s);
			if (sb.length() > 0)
				sb.append(":");
			if (s < 10)
				sb.append("0");
			sb.append(second);
		}
		return sb.toString();
	}

	public static boolean isRunNian(int year){
		boolean runnian;
		if (year % 100 == 0){
			runnian = year%400 == 0;
		}else{
			runnian = year%4 == 0;
		}

		return runnian;
	}

	public static int dayCountOfYear(int year){
		return isRunNian(year)?366:365;
	}


	public static int dayCountOfMonth(int year, int month){
		if (ConvertUtils.oneOfInt(month, new int[]{1,3,5,7,8,10,12}))
			return 31;
		if (month == 2){
			return isRunNian(year)?29:28;
		}
		return 30;
	}

	public static int getYearWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); // Set Monday as the first day of the week
		cal.setTime(date);
		return cal.getWeekYear() * 100 + cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getYearWeek(int date){
		return getYearWeek(int2date(date));
	}


	public static String timestamp2String(int time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		String times = sdr.format(new Date(time * 1000L));
		return times;
	}

	public static String timestamp2String(long time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdr.format(new Date(time));
	}

	public static String timestamp2dateShow(long time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
		return sdr.format(new Date(time));
	}

	public static void main(String[] args){
		/*System.out.println(String.format("%s: %s", 20151231, getYearWeek(20151231)));
		System.out.println(String.format("%s: %s", 20160101, getYearWeek(20160101)));
		System.out.println(String.format("%s: %s", 20160103, getYearWeek(20160103)));
		System.out.println(String.format("%s: %s", 20160104, getYearWeek(20160104)));
		System.out.println(String.format("%s: %s", 20160703, getYearWeek(20160703)));
		System.out.println(String.format("%s: %s", 20160704, getYearWeek(20160704)));
		System.out.println(String.format("%s: %s", 20160709, getYearWeek(20160709)));
		System.out.println(String.format("%s: %s", 20160710, getYearWeek(20160710)));
		System.out.println(String.format("%s: %s", DateTimeUtils.today2int(), getYearWeek(new Date())));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160703))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160704))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160705))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160706))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160707))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160708))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160709))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160710))));
		System.out.println(DateTimeUtils.date2int(DateTimeUtils.getThisMonday(DateTimeUtils.int2date(20160711))));
		*/
	}

	public final static  String[] WEEK_CN_ARRAY = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	public static int getWeekOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0){
			w = 0;
		}
		return w;
	}
	public static String getWeekCnOfDate(Date date) {
		int w =getWeekOfDate(date);
		return WEEK_CN_ARRAY[w];
	}

	public static Timestamp getCurrTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	public static Timestamp date2timestamp(Date date){
		return new Timestamp(date.getTime());
	}
}
