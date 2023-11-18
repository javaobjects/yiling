package com.yiling.erp.client.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil
{
    private static final Log logger = LogFactory.getLog(DateUtil.class);
    public static final String YEAR_SPLIT_FORMAT = "yyyy";
    public static final String MONTH_SPLIT_FORMAT = "yyyy-MM";
    public static final String DAY_SPLIT_FORMAT = "yyyy-MM-dd";
    public static final String MONTH_FORMAT = "yyyyMM";
    public static final String DAY_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_SPLIT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date convertString2Date(String dateStr, String formatStr)
    {
        if(StringUtil.isEmpty(dateStr)){
            return null;
        }
        if("null".equals(dateStr)){
            return null;
        }
        DateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
            logger.debug(date.toString());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static boolean hasConvertDate(String dateStr, String formatStr)
    {
        DateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
            logger.debug(date.toString());
            return true;
        } catch (ParseException e) {
            logger.debug(e.getMessage());
        }
        return false;
    }

    public static Date currentDate()
    {
        long dateLong = System.currentTimeMillis();
        Date date = new Date(dateLong);
        return date;
    }

    public static Date currentDate(int day, int hour, int minute)
    {
        long addTime = day * 24 * 60 * 60 * 1000;
        addTime += hour * 60 * 60 * 1000;
        addTime += minute * 60 * 1000;

        long dateLong = System.currentTimeMillis() + addTime;
        Date date = new Date(dateLong);
        return date;
    }

    public static Timestamp currentTimeStamp()
    {
        long dateLong = System.currentTimeMillis();
        Timestamp date = new Timestamp(dateLong);
        return date;
    }

    public static Timestamp dateToTimeStamp(Date date) {
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }

    public static String currentDate2String(String formatStr)
    {
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = currentDate();
        return convertDate2String(date, formatStr);
    }

    public static String currentDate2Timestamp(String formatStr)
    {
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyyMMddHHmmss";
        }
        Date date = currentDate();
        return convertDate2String(date, formatStr);
    }

    public static String convertDate2String(Date date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        try {
            dateStr = format.format(date);
            logger.debug(date.toString());
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return dateStr;
    }

    public static String convertDate2String(Date date, String formatStr)
    {
        DateFormat format = new SimpleDateFormat(formatStr);
        String dateStr = "";
        try {
            dateStr = format.format(date);
            logger.debug(date.toString());
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return dateStr;
    }

    public static String convertTimeStamp2String(Timestamp date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        try {
            dateStr = format.format(date);
            logger.debug(date.toString());
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return dateStr;
    }

    public static String convertTimeStamp2String(Timestamp date, String formatStr)
    {
        DateFormat format = new SimpleDateFormat(formatStr);
        String dateStr = "";
        try {
            dateStr = format.format(date);
            logger.debug(date.toString());
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return dateStr;
    }

    public static long dateDiffer(Date date)
    {
        long nowTime = System.currentTimeMillis();
        long compareTime = date.getTime();
        return Math.abs(nowTime - compareTime);
    }

    public static boolean dateDiffer(Date start, Date end)
    {
        long now = System.currentTimeMillis();
        long startLong = start.getTime();
        long endLong = end.getTime();
        if ((startLong <= now) && (now <= endLong)) {
            return true;
        }
        return false;
    }

    public static Date before(Date dtDate, long lDays)
    {
        long lCurrentDate = 0L;
        lCurrentDate = dtDate.getTime() - lDays * 24L * 60L * 60L * 1000L;
        Date dtBefor = new Date(lCurrentDate);
        return dtBefor;
    }

    public static Date dateLaterMins(String executeTime, int quartzBlockTime)
    {
        Date dateAfter10Mins = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if ((executeTime != null) && (StringUtil.isNotEmpty(executeTime)))
                date = sdf.parse(executeTime);
            else {
                date = new Date();
            }
            long threeminlater = date.getTime() + quartzBlockTime * 60000;
            dateAfter10Mins = new Date(threeminlater);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return dateAfter10Mins;
    }

    public static Date formatMinTimeInDate(String day, String format)
    {
        if ((format == null) || ("".equals(format.trim()))) {
            return null;
        }
        Date date = convertString2Date(day, format);
        if (date == null) {
            return date;
        }
        return formatMinTimeInDate(date);
    }

    public static Date formatMinTimeInDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return formatMinTimeInDate(calendar);
    }

    public static Date formatMinTimeInDate(Calendar calendar)
    {
        calendar.set(11, calendar.getActualMinimum(11));

        calendar.set(12, calendar.getActualMinimum(12));

        calendar.set(13, calendar.getActualMinimum(13));

        return calendar.getTime();
    }

    public static Date formatMaxTimeInDate(String day, String format)
    {
        if (StringUtil.isEmpty(format)) {
            return null;
        }
        Date date = convertString2Date(day, format);
        if (date == null) {
            return null;
        }
        return formatMaxTimeInDate(date);
    }

    public static Date formatMaxTimeInDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return formatMaxTimeInDate(calendar);
    }

    public static Date formatMaxTimeInDate(Calendar calendar)
    {
        calendar.set(11, calendar.getActualMaximum(11));

        calendar.set(12, calendar.getActualMaximum(12));

        calendar.set(13, calendar.getActualMaximum(13));

        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse("2014-06-08 21:36:56");
        Date end = sdf.parse("2014-06-9 21:37:12");

        Date date = new Date(1438412400000L);
        System.out.println(sdf.format(date));

        long millionSeconds = sdf.parse("2015-08-01 14:40:00").getTime();

        System.out.println(millionSeconds);
    }
}