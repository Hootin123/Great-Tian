package com.xtr.comm.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public final class DateUtil {

    /**
     * 日期时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateTimeFormatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat dayFormatterLine = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat monthFormatterLine = new SimpleDateFormat("MM");
    public static SimpleDateFormat dateFormatterNoLine = new SimpleDateFormat("yyyyMMdd");


    /**
     * 日期时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static String dateTimeString = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式 yyyy-MM-dd
     */
    public static String dateString = "yyyy-MM-dd";

    public static final String DATETIMENOSPLITFORMAT = "yyyyMMddHHmmss";
    public static final String DATESHORTFORMAT = "yyyyMMdd";
    public static final String DATEYEARFORMATTER = "yyyy-MM-dd";
    public static final String DATEMONTHFORMAT = "yyyyMM";
    public static final String TIMESHORTFORMAT = "HHmmss";
    public static final String DATEMONTHFORLINE = "yyyy-MM";
    private static Log log = LogFactory.getLog(DateUtil.class);

    private DateUtil() {
    }

    /**
     * 间隔时间的类型：天
     */
    public static final int BETWEEN_DAYS = 1;

    /**
     * 间隔时间的类型：小时
     */
    public static final int BETWEEN_HOURS = 2;

    /**
     * 间隔时间的类型：分钟
     */
    public static final int BETWEEN_MINS = 3;

    /**
     * 默认时间字符串的格式
     */
    public static final String DEFAULT_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * 生成日期解析对象
     *
     * @param pattern 转换格式
     * @return DateFormat 日期解析对象
     */
    private static DateFormat doDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 日期转换到字符串
     *
     * @param paramDate 要转换的日期
     * @param pattern   转换格式：例：yyyy-MM-dd
     * @return String 日期字符串
     */
    public static String dateToString(Date paramDate, String pattern) {
        return doDateFormat(pattern).format(paramDate);
    }

    /**
     * 字符串转换到日期
     *
     * @param dateStr 日期字符串
     * @param pattern 转换格式：例：yyyy-MM-dd
     * @return Date 转换后的日期
     */
    public static Date stringToDate(String dateStr, String pattern) {
        try {
            return doDateFormat(pattern).parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 以指定格式返回指定日期的字符串
     *
     * @param pattern - 日期显示格式
     * @param date    - 需要格式 化的时间
     * @return the formatted date-time string
     * @see java.text.SimpleDateFormat
     */
    public static String formatDateTime(String pattern, java.util.Date date) {
        String strDate = null;
        String strFormat = pattern;
        SimpleDateFormat dateFormat = null;

        if (date == null)
            return "";
        dateFormat = new SimpleDateFormat(strFormat);
        strDate = dateFormat.format(date);

        return strDate;
    }


    /**
     * 获取当前日期(字符串格式)
     *
     * @param pattern 转换格式：例：yyyy-MM-dd
     * @return String 日期字符串
     */
    public static String getCurrDate(String pattern) {
        return dateToString(new Date(), pattern);
    }

    /**
     * 获取当前日期(日期格式)
     *
     * @param pattern 转换格式：例：yyyy-MM-dd
     * @return Date 日期
     */
    public static Date getCurrDateOfDate(String pattern) {
        return stringToDate(dateToString(new Date(), pattern), pattern);
    }

    /**
     * 获取日期是星期几
     *
     * @param paramDate 参数日期
     * @param retFormat 返回格式：0、表示返回数字格式 1、表示返回中文格式
     * @return String 星期几
     */
    public static String getDayOfWeek(Date paramDate, int retFormat) {
        Calendar c = Calendar.getInstance();
        c.setTime(paramDate);
        int dayOfWeek = (c.get(Calendar.DAY_OF_WEEK) == 1) ? 7 : c.get(Calendar.DAY_OF_WEEK) - 1;
        String dayOfWeekStr = null;
        switch (dayOfWeek) {
            case 1:
                dayOfWeekStr = (0 == retFormat) ? "1" : "一";
                break;
            case 2:
                dayOfWeekStr = (0 == retFormat) ? "2" : "二";
                break;
            case 3:
                dayOfWeekStr = (0 == retFormat) ? "3" : "三";
                break;
            case 4:
                dayOfWeekStr = (0 == retFormat) ? "4" : "四";
                break;
            case 5:
                dayOfWeekStr = (0 == retFormat) ? "5" : "五";
                break;
            case 6:
                dayOfWeekStr = (0 == retFormat) ? "6" : "六";
                break;
            case 7:
                dayOfWeekStr = (0 == retFormat) ? "7" : "日";
                break;
        }
        return dayOfWeekStr;
    }

    /**
     * 指定日期几天后或者几天前的日期
     *
     * @param paramDate 指定日期
     * @param days      天数
     * @return Date 几天后或者几天前的日期
     */
    public static Date addDate(Date paramDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paramDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 指定日期几月后或者几月前的日期
     *
     * @param paramDate 指定日期
     * @param months    月数
     * @return Date 几月后或者几月前的日期
     */
    public static Date addDateOfMonth(Date paramDate, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paramDate);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 根据指定日期获取指定日期所在周的开始日期和结束日期(星期一、星期天)
     *
     * @param paramDate 指定日期
     * @return String[] 开始日期和结束日期数组
     */
    public static String[] getWeekStartAndEndDate(Date paramDate) {
        String[] retAry = new String[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paramDate);
        // 以周一为一周的开始
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        retAry[0] = dateToString(calendar.getTime(), "yyyy-MM-dd");
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        retAry[1] = dateToString(calendar.getTime(), "yyyy-MM-dd");

        return retAry;
    }

    /**
     * 根据指定日期获取指定日期所在月的第一天和最后一天
     *
     * @param paramDate 指定日期
     * @return String[] 第一天和最后一天数组
     */
    public static String[] getMonthStartAndEndDate(Date paramDate) {
        String[] retAry = new String[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paramDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        retAry[0] = dateToString(calendar.getTime(), "yyyy-MM-dd");
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        retAry[1] = dateToString(calendar.getTime(), "yyyy-MM-dd");

        return retAry;
    }

    /**
     * 获取指定两个日期相差的天数
     *
     * @param paramDate1 指定日期1
     * @param paramDate2 指定日期2
     * @return int 相差天数
     */
    public static int getDiffDaysOfTwoDate(String paramDate1, String paramDate2) {
        Date date1 = stringToDate(paramDate1, "yyyy-MM-dd");
        Date date2 = stringToDate(paramDate2, "yyyy-MM-dd");

        Long diffTimes = date1.getTime() - date2.getTime();
        Long diffDays = diffTimes / (3600 * 1000 * 24);

        return Math.abs(diffDays.intValue());
    }

    /**
     * 获取指定两个日期相差的天数(固定顺序,可以取负值)
     *
     * @param paramDate1 指定日期1
     * @param paramDate2 指定日期2
     * @return int 相差天数
     */
    public static int getDiffDaysOfTwoDateByNegative(String paramDate1, String paramDate2) {
        Date date1 = stringToDate(paramDate1, "yyyy-MM-dd");
        Date date2 = stringToDate(paramDate2, "yyyy-MM-dd");

        Long diffTimes = date1.getTime() - date2.getTime();
        Long diffDays = diffTimes / (3600 * 1000 * 24);

        return diffDays.intValue();
    }

   /*
   * <p>获取指定两个时间相差的秒数</p>
   * @auther 何成彪
   * @createTime2016/6/24 14:30
   */

    public static long getDiffSecondsofTwoDate(String paramDate1, String paramDate2) {
        Date date1 = stringToDate(paramDate1, "yyyy-MM-dd hh:mm:ss");
        Date date2 = stringToDate(paramDate2, "yyyy-MM-dd hh:mm:ss");
        Long diffTimes = date1.getTime() - date2.getTime();
        Long diffSeconds = diffTimes / 1000;
        return Math.abs(diffSeconds);
    }


    /**
     * 获取指定日期相差月份数
     *
     * @param paramDate1 指定日期1
     * @param paramDate2 指定日期2
     * @return int 相差月份数 注：日期所在月都算一月
     */
    public static int getDiffMonthsOfTwoDate(String paramDate1, String paramDate2) {
        // 指定日期1的年份、月份
        int tempYear1 = Integer.parseInt(paramDate1.substring(0, 4));
        int tempMonth1 = Integer.parseInt(paramDate1.substring(5, 7));

        // 指定日期2的年份、月份
        int tempYear2 = Integer.parseInt(paramDate2.substring(0, 4));
        int tempMonth2 = Integer.parseInt(paramDate2.substring(5, 7));

        return Math.abs((tempYear1 * 12 + tempMonth1) - (tempYear2 * 12 + tempMonth2)) + 1;
    }

    /**
     * 获取指定日期所在月有多少天
     *
     * @param paramDate 指定日期(yyyy-MM格式)
     * @return int 指定日期所在月有多少天
     */
    public static int getDaysOfMonths(String paramDate) {
        int days = 0;
        try {
            Date date = doDateFormat("yyyy-MM").parse(paramDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return days;
    }

    /**
     * 获取两个月份之间所有的月份数组
     *
     * @param startMonth 开始月份
     * @param endMonth   结束月份
     * @return String[] 两个月份之间所有的月份数组 注：日期格式为yyyy-MM
     */
    public static String[] getMonthAryOfTwoMonth(String startMonth, String endMonth) {
        String[] monthAry = null;

        if (startMonth == endMonth) {
            monthAry = new String[1];
            monthAry[0] = startMonth;
        } else {
            int diffMonth = getDiffMonthsOfTwoDate(startMonth, endMonth);
            monthAry = new String[diffMonth];
            if (diffMonth == 2) {
                monthAry[0] = startMonth;
                monthAry[1] = endMonth;
            } else {
                monthAry[0] = startMonth;

                int endIndex = diffMonth - 1;
                Date startMonthDate = DateUtil.stringToDate(startMonth, "yyyy-MM");
                for (int i = 1; i < endIndex; i++) {
                    Date tmpDate = DateUtil.addDateOfMonth(startMonthDate, i);
                    String tmpDateStr = DateUtil.dateToString(tmpDate, "yyyy-MM");
                    monthAry[i] = tmpDateStr;
                }
                monthAry[endIndex] = endMonth;
            }
        }
        return monthAry;
    }

    /**
     * 判断给定的时间加上一段时间长度后，是否超过当前时间
     *
     * @param timeStr 给定时间字符串，格式为默认格式：yyyy-MM-dd HH:mm:ss
     * @param time    时间长度，单位为分钟
     * @return true：超过当前时间 false：没有超过当前时间
     * @throws ParseException
     */
    public static boolean isAccordTime(String timeStr, int time) throws ParseException {
        if (null == timeStr) {
            return false;
        }
        Calendar c = Calendar.getInstance();

        c.setTime(string2Date(timeStr));

        c.add(Calendar.MINUTE, time);

        return c.after(Calendar.getInstance());
    }

    /**
     * 取得两个日期字符串相隔时间长度（目前只支持天数和小时数和分钟）。格式为：yyyy-MM-dd HH:mm:ss， 如果输入的日期格式年月日不对
     * ,抛出异常。时间参数没有顺序，可从小到大或者从大到小。
     *
     * @param timeStringOne 开始时间
     * @param timeStringTwo 结束时间
     * @param type          取得的时间单位：1：取间隔的天数 2：取间隔的小时数
     * @return 间隔时间长度
     * @throws ParseException
     */
    public static int getBetweenTimes(String timeStringOne, String timeStringTwo, int type) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT_STR);

        Date d1 = format.parse(timeStringOne);
        Date d2 = format.parse(timeStringTwo);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        // 保证时间顺序
        if (c1.after(c2)) {
            c1.setTime(d2);
            c2.setTime(d1);
        }

        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        int betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);

        int betweenHours = c2.get(Calendar.HOUR_OF_DAY) - c1.get(Calendar.HOUR_OF_DAY);

        int betweenMins = c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE);

        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        if (BETWEEN_DAYS == type) {
            return betweenDays;
        } else if (BETWEEN_HOURS == type) {
            betweenHours = betweenDays * 24 + betweenHours;

            return betweenHours;
        } else {
            betweenMins = betweenDays * 24 * 60 + betweenHours * 60 + betweenMins;
            return betweenMins;
        }
    }

    /**
     * 将Date日期转换为String
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String date2String(Date date, String formatStr) {
        if (null == date || null == formatStr) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);

        return df.format(date);
    }

    /**
     * 将时间字符串转换为日期，字符串格式为默认格式
     *
     * @param timeStr 时间字符串
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String timeStr) throws ParseException {
        if (null == timeStr) {
            return null;
        }
        return string2Date(timeStr, DEFAULT_FORMAT_STR);
    }

    /**
     * 将时间字符串转换为日期
     *
     * @param timeStr   时间字符串
     * @param formatStr 字符串格式，如:yyyy-mm-dd
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String timeStr, String formatStr) throws ParseException {
        if (null == timeStr || null == formatStr) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);

        return df.parse(timeStr);
    }

    /**
     * @param date 当前日期
     * @return 当前日期的前一个月
     */
    public static String getPriviousDateBySysDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, -1);//月份减一
        return sdf.format(calendar.getTime());
    }

    /**
     * formatCurrentDate 2015-06-25
     *
     * @return
     */
    public static String formatCurrentDate() {
        return dateFormatter.format(new Date());
    }

    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    /**
     * formatCurrentDatetime 2015-06-25 21:15:21
     *
     * @return
     */
    public static String formatCurrentDatetime() {
        return dateTimeFormatter.format(new Date());
    }

    public static String formatDatetime(Date date) {
        return dateTimeFormatter.format(date);
    }

    public static String formatCurrentTime() {
        return timeFormatter.format(new Date());
    }

    /**
     * formatCurrentTime 21:15:21
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return timeFormatter.format(date);
    }

    /**
     * 当前时间，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDatetime() {
        return dateTimeFormatter.format(new Date());
    }

    /**
     * 当前时间，格式：yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentDatetime2() {
        return dateTimeFormatter2.format(new Date());
    }

    /**
     * 当前时间，格式：yyyyMMdd
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return dateFormatter.format(new Date());
    }

    /**
     * 将时间字符串转换为日期，字符串格式为默认格式
     * <p/>
     * 时间字符串
     *
     * @return
     * @throws ParseException
     */
    public static Date getCurrentDate() {
        try {
            return string2Date(StringUtils.substring(getCurrentDateStr(), 0, 10), "yyyy-MM-dd");
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取交易日期字符串 8位 yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String getTxnDateStr(Date date) {
        return pareDate(date, DATESHORTFORMAT);
    }

    /**
     * date 转String
     *
     * @param pattern
     * @return
     */
    public static String pareDate(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATESHORTFORMAT;
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 计算两个日期间隔是时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String dateDiff(Long startTime, Long endTime) {

        //按照传入的格式生成一个simpledateformate对象
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        //获得两个时间的毫秒时间差异
//            long diff = dateTimeFormatter.parse(endTime).getTime() - dateTimeFormatter.parse(startTime).getTime();
        long diff = endTime - startTime;
        long day = Math.abs(diff / nd);//计算差多少天
        long hour = Math.abs(diff % nd / nh);//计算差多少小时
        long min = Math.abs(diff % nd % nh / nm);//计算差多少分钟
        long sec = Math.abs(diff % nd % nh % nm / ns);//计算差多少秒//输出结果
//            System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
        return day + "天" + hour + "时" + min + "分" + sec + "秒";
    }

    public static Date formateDateToYYYYMMDD(Date date) {
        DateFormat dateFormat = doDateFormat(dateString);
        String format = dateFormat.format(date);
        try {
            return dateFormat.parse(format);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }

    public static boolean isWeekend(Date date) {
        if (date == null)
            return false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        return weekday == Calendar.SATURDAY || weekday == Calendar.SUNDAY;
    }

    public static Date addWorkDay(Date date, int add) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        if (add == 0) {
            return instance.getTime();
        }
        int index = 0;
        while (index < Math.abs(add)) {

            instance.add(Calendar.DAY_OF_MONTH, (add < 0 ? -1 : 1));
            if (isWeekend(instance.getTime())) {
                continue;
            } else {
                index++;
            }
        }
        return instance.getTime();
    }

    private static final SimpleDateFormat[] allStyle = {new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy.MM.dd"), new SimpleDateFormat("yyyy/MM/dd"), new SimpleDateFormat("yyyyMMdd")};

    public static Date formatAllStyle(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        for (SimpleDateFormat simpleDateFormat : allStyle) {
            try {
                return simpleDateFormat.parse(str);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public static final SimpleDateFormat sbtSimpleDateFormat = new SimpleDateFormat("yyyyMM");

    public static void main(String[] args) throws InterruptedException {
        Long dateTime1 = System.currentTimeMillis();
        Thread.sleep(1000);
        Long dateTime2 = System.currentTimeMillis();
        System.out.println(DateUtil.dateDiff(dateTime1, dateTime2));
        System.out.println(dateTime1);
        System.out.println(dateTime2);
    }

}
