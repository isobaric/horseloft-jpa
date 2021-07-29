package com.horseloft.jpa.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Date: 2020/1/4 13:50
 * User: YHC
 * Desc:
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN_V1 = "yyyyMMddHHmmssSSS";
    public static final String DATE_TIMEZ_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String DATEPATTERN = "yyyyMMdd";
    public static final String DATE_PATTERN_V1 = "MMddHHmmss";
    public static final String HOURS_PATTERN = "HH:mm:ss";
    public static final String DATE_TIME_CHINESE = "yyyy年M月dd日";

    /**
     * 获取当前日期
     */
    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前日期
     */
    public static Date getDate() {
        return new DateTime().toDate();
    }

    /**
     * 日期转换dateTime
     * @param date
     * @return
     */
    public static DateTime getDateTime(Date date) {
        return new DateTime(date);
    }

    /**
     * 日期转换为字符串yyyy-MM-dd
     * @param date
     * @return
     */
    public static String dateFormatString(Date date) {
        return DateUtils.date2String(date, null);
    }

    /**
     * 指定格式日期转字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String date2String(Date date, String pattern) {
        return DateUtils.getDateTime(date).toString(StringUtils.isNotBlank(pattern) ? pattern : DATE_PATTERN);
    }

    /**
     * 得到以yyyyMMdd格式表示的当前日期字符串
     */
    public static String getDatePatternConnect() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
        return sdf.format(new Date());
    }

    /**
     * 得到以yyyy-MM-dd HH:mm:ss格式表示的当前日期字符串
     * @return
     */
    public static String getDateTimePatternConnect() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        return sdf.format(new Date());
    }

    /**
     * 得到以yyyy-MM-dd HH:mm:ss格式表示的当前日期字符串
     * @param date
     * @return
     */
    public static String getDateTimePatternConnect(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     * @param date
     * @param pattern
     * @return
     */
    public static Date stringFormatDate(String date, String pattern) {
        DateTimeFormatter format = DateTimeFormat.forPattern(StringUtils.isNotBlank(pattern) ? pattern : DATE_PATTERN);
        DateTime dateTime = DateTime.parse(date, format);
        return dateTime.toDate();
    }

    /**
     * 字符串时间
     * @param str
     * @return
     */
    public static Date parse(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringFormatDate2(String date, String pattern) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
        LocalDate holiday = LocalDate.parse(date, formatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = holiday.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date stringFormatDateTime(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 字符串转日期
     * @param date
     * @return
     */
    public static Date stringFormatDate(String date) {
        return DateUtils.stringFormatDate(date, null);
    }

    /**
     * 计算两个日期时间差(天)
     * @param date
     * @param date1
     * @return
     */
    public static int gapDays(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.days());
        return p.getDays();
    }

    /**
     * 计算两个日期时间差(月)
     * @param date
     * @param date1
     * @return
     */
    public static int gapMonths(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.months());
        return p.getMonths();
    }

    /**
     * 计算两个日期时间差(年)
     * @param date
     * @param date1
     * @return
     */
    public static int gapYears(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.years());
        return p.getYears();
    }

    /**
     * 计算两个日期时间差(小时)
     * @param date
     * @param date1
     * @return
     */
    public static int gapHours(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.hours());
        return p.getHours();
    }

    /**
     * 计算两个日期时间差(分)
     * @param date
     * @param date1
     * @return
     */
    public static int gapMinutes(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.minutes());
        return p.getMinutes();
    }

    /**
     * 计算两个日期时间差(秒)
     * @param date
     * @param date1
     * @return
     */
    public static int gapSeconds(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.seconds());
        return p.getSeconds();
    }

    /**
     * 计算两个日期时间差(秒)
     * @param date
     * @param date1
     * @return
     */
    public static long gapMillis(Date date, Date date1) {
        Duration d = new Duration(DateUtils.getDateTime(date), DateUtils.getDateTime(date1));
        return d.getStandardSeconds();
    }

    /**
     * 获取N天前日期
     * @param date
     * @param day
     * @return
     */
    public static Date minusDays(Date date, int day) {
        return DateUtils.getDateTime(date).minusDays(day).toDate();
    }

    /**
     * 获取N天后日期
     * @param date
     * @param day
     * @return
     */
    public static Date plusDays(Date date, int day) {
        return DateUtils.getDateTime(date).plusDays(day).toDate();
    }

    /**
     * 获取N个月前日期
     * @param date
     * @param month
     * @return
     */
    public static Date minusMonths(Date date, int month) {
        return DateUtils.getDateTime(date).minusMonths(month).toDate();
    }

    /**
     * 获取N个月后日期
     * @param date
     * @param month
     * @return
     */
    public static Date plusMonths(Date date, int month) {
        return DateUtils.getDateTime(date).plusMonths(month).toDate();
    }

    /**
     * 获取N年前日期
     * @param date
     * @param year
     * @return
     */
    public static Date minusYears(Date date, int year) {
        return DateUtils.getDateTime(date).minusYears(year).toDate();
    }

    /**
     * 获取N年后日期
     * @param date
     * @param year
     * @return
     */
    public static Date plusYears(Date date, int year) {
        return DateUtils.getDateTime(date).plusYears(year).toDate();
    }

    /**
     * 获取N周前日期
     * @param date
     * @param week
     * @return
     */
    public static Date minusWeeks(Date date, int week) {
        return DateUtils.getDateTime(date).minusWeeks(week).toDate();
    }

    /**
     * 获取N周前日期
     * @param date
     * @param week
     * @return
     */
    public static Date plusWeeks(Date date, int week) {
        return DateUtils.getDateTime(date).plusWeeks(week).toDate();
    }

    /**
     * 获取N小时前日期
     * @param date
     * @param hour
     * @return
     */
    public static Date minusHours(Date date, int hour) {
        return DateUtils.getDateTime(date).minusHours(hour).toDate();
    }

    /**
     * 获取N分钟前日期
     * @param date
     * @param minute
     * @return
     */
    public static Date minusMinutes(Date date, int minute) {
        return DateUtils.getDateTime(date).minusMinutes(minute).toDate();
    }

    /**
     * 获取N小时后日期
     * @param date
     * @param hour
     * @return
     */
    public static Date plusHours(Date date, int hour) {
        return DateUtils.getDateTime(date).plusHours(hour).toDate();
    }

    /**
     * 是否比系统时间大
     * @param date
     * @return
     */
    public static boolean isAfterNow(Date date) {
        return DateUtils.getDateTime(date).isAfterNow();
    }

    /**
     * 是否比系统时间小
     * @param date
     * @return
     */
    public static boolean isBeforeNow(Date date) {
        return DateUtils.getDateTime(date).isBeforeNow();
    }

    /**
     * 是否与系统时间相等
     * @param date
     * @return
     */
    public static boolean isEqualNow(Date date) {
        return DateUtils.getDateTime(date).isEqualNow();
    }

    /**
     * 时间比较date大于date1
     * @param date
     * @param date1
     * @return
     */
    public static boolean isEqualNow(Date date, Date date1) {
        return DateUtils.getDateTime(date).isAfter(DateUtils.getDateTime(date1));
    }

    /**
     * 时间比较date小于date1
     * @param date
     * @param date1
     * @return
     */
    public static boolean isBefore(Date date, Date date1) {
        return DateUtils.getDateTime(date).isBefore(DateUtils.getDateTime(date1));
    }

    /**
     * 时间比较date等于date1
     * @param date
     * @param date1
     * @return
     */
    public static boolean isEqual(Date date, Date date1) {
        return DateUtils.getDateTime(date).isEqual(DateUtils.getDateTime(date1));
    }

    /**
     * 获取日期中年份
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        return new DateTime(date).getYear();
    }

    /**
     * 获取日期中月份
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        return new DateTime(date).getMonthOfYear();
    }

    /**
     * 获取日期中几点
     * @param date
     * @return
     */
    public static int getHourOfDay(Date date) {
        return new DateTime(date).getHourOfDay();
    }

    /**
     * 获取日期中日份
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        return new DateTime(date).getDayOfMonth();
    }


    public static int getHours(Date date) {
        return new DateTime(date).getHourOfDay();
    }

    public static int getMinute(Date date) {
        return new DateTime(date).getMinuteOfHour();
    }

    public static int getSecond(Date date) {
        return new DateTime(date).getSecondOfMinute();
    }

    /**
     * 判断是否为闰年
     * @param date
     * @return
     */
    public static boolean isLeap(Date date) {
        DateTime.Property month = DateUtils.getDateTime(date).monthOfYear();
        return month.isLeap();
    }

    /**
     * 获取当前时间到第二天凌晨时间剩余秒数
     */
    public static long surplusSeconds() {
        Date date = DateUtils.plusDays(new Date(), 1);
        DateTime dateTime1 = new DateTime(DateUtils.stringFormatDate(DateUtils.date2String(date, "yyyy-MM-dd")));
        return DateUtils.gapMillis(new Date(), dateTime1.toDate());
    }

    /**
     * 获取指定时间年
     * @param date
     * @return
     */
    public static Integer getYear(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getYear()));
    }

    /**
     * 获取指定时间月
     * @param date
     * @return
     */
    public static Integer getMonth(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getMonth().getValue()));
    }

    /**
     * 获取指定时间日
     * @param date
     * @return
     */
    public static Integer getDayOfMonth(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getDayOfMonth()));
    }

    /**
     * 得到以yyyy-MM-dd格式
     * @return
     */
    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(new Date());
    }


    /**
     * 得到以yyyyMMddHHmmss格式表示的当前日期字符串
     * @param date
     * @return
     */
    public static String getAllDateV1(Object date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN_V1);
        return sdf.format(date);
    }

    /**
     * 得到以pattern格式表示的当前日期字符串
     * @param date 日期
     * @param pattern 类型
     * @return string
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 根据身份证号计算年龄
     * @param idNo
     * @return
     */
    public static long getAgeByIdNo(String idNo) {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(idNo.substring(6, 14),
                java.time.format.DateTimeFormatter.ofPattern(DATEPATTERN));
        return ChronoUnit.YEARS.between(birthDate, now);
    }

    /**
     * 根据身份证号计算毫秒
     * @param idNo
     * @return
     */
    public static long getMillisByIdNo(String idNo) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
        long time = 0L;
        try {
            time = sdf.parse(idNo.substring(6, 14)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.getWeekYear();
        calendar.getFirstDayOfWeek();
        // 获得当前的年
        int weekYear = calendar.get(Calendar.YEAR);
        // 获得当前日期属于今年的第几周
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        // 获得指定年的第几周的开始日期
        calendar.setWeekDate(weekYear, weekOfYear, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    /**
     * 获取 当月几号
     * @param date
     * @return
     */
    public static Integer getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 设置日期时间为23:59:59
     * @param date
     * @return
     */
    public static Date setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm:ss转为指定的格式
     * @param timeStr "2020-02-02 10:10:10"
     * @param format "M月dd日"
     * @return
     */
    public static String stringTime2String(String timeStr, String format){
        if (timeStr == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        Date date;
        try {
            date = simpleDateFormat.parse(timeStr);
        } catch (Exception e) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * localDateTime转date
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    /**
     * 获取某个日期的开始时间
     * @param d
     * @return
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取上周的开始时间
     * @return
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取今天是今年的多少周
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int mouth = calendar.get(Calendar.MONTH);
        // JDK think 2015-12-31 as 2016 1th week
        //如果月份是12月，且求出来的周数是第一周，说明该日期实质上是这一年的第53周，也是下一年的第一周
        if (mouth >= 11 && week <= 1) {
            week += 52;
        }
        return week;
    }

    /**
     *
     * @param date
     * @return
     */
    public static int getYearOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int weeksBetween(Date fromDate, Date toDate) {
        if (fromDate.before(toDate)) {
            Date temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }
        Integer weekNum = (getWeekOfYear(fromDate) - getWeekOfYear(toDate))
                + (getYearOfDate(fromDate) - getYearOfDate(toDate)) * 52;
        return ++weekNum;
    }

    /**
     * long转日期
     * @param param
     * @return
     */
    public static Date longToDate(long param){
        return new Date(param * 1000L);
    }

    /**
     * 将时间戳转换为时间
     * @param time
     * @return
     */
    public static String stampToDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time * 1000L));
    }

    //当前时间N天后的时毫秒间戳
    public static Long getMillisTimeAfterDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTimeInMillis();
    }

    //当前时间N小时后的时毫秒时间戳
    public static Long getMillisTimeAfterHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR ) + hour);
        return calendar.getTimeInMillis();
    }

    //日期字符串
    public static boolean isDate(String date) {
        return DateValid(date, DATE_PATTERN);
    }

    //日期字符串
    public static boolean isDatetime(String datetime) {
        return DateValid(datetime, DATE_TIME_PATTERN);
    }

    //字符串格式日期验证
    public static boolean DateValid(String strDate, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            // 设置lenient为false. 否则会比较宽松地验证日期，如2018-02-29会被接受，并转换成2018-03-01
            format.setLenient(false);
            format.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
