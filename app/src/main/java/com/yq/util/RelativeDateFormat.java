package com.yq.util;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式转化
 */
public class RelativeDateFormat {

    private static final String TAG = "RelativeDateFormat";
    private  static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    /*   public static void main(String[] args) throws ParseException {
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
           Date date = format.parse("2015-10-1 18:00:33");
           System.out.println(format(date)+"....");
       }
   */

    public static String  timeStamp2Date(long time ) {
        //mill为你龙类型的时间戳
        Long mills = time;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills * 1000);//转换为毫秒
        Date date = calendar.getTime();
        return format(date);
    }
    public static String format(Date date) {

        String dateString = format.format(date);
        return dateString;

    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

}
