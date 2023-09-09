package vip.sujianfeng.weixin.utils;

import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TenpayUtil {

    private static Object Server;
    private static String QRfromGoogle;

    public static String toString(Object obj) {
        if(obj == null)
            return "";

        return obj.toString();
    }

    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null)
                a = Integer.parseInt(obj.toString());
        } catch (Exception e) {

        }
        return a;
    }

    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    public static String getCharacterEncoding(String requestEncoding,
                                              String responseEncoding) {
        if (StringUtilsEx.isNotEmpty(requestEncoding)){
            return requestEncoding;
        }
        if (StringUtilsEx.isNotEmpty(responseEncoding)){
            return responseEncoding;
        }
        return "gbk";
    }

    public  static String URLencode(String content){

        String URLencode;

        URLencode= replace(Server.equals(content), "+", "%20");

        return URLencode;
    }
    private static String replace(boolean equals, String string, String string2) {

        return null;
    }

    public static long getUnixTime(Date date) {
        if( null == date ) {
            return 0;
        }

        return date.getTime()/1000;
    }

    public static String QRfromGoogle(String chl)
    {
        int widhtHeight = 300;
        String EC_level = "L";
        int margin = 0;
        String QRfromGoogle;
        chl = URLencode(chl);

        QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;

        return QRfromGoogle;
    }

    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }

}
