package com.xtr.comm.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.util.IntegerField;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>用于测试代码产生的随机数据</p>
 *
 * @author 任齐
 * @createTime: 2016/6/30 18:45
 */
public final class RandomDataUtil {

    /**
     * 获取随机数的编号
     *
     * @return
     */
    public static String getRandomID() {
        return RandomStringUtils.randomAlphabetic(3).toUpperCase() + RandomStringUtils.randomNumeric(8);
    }

    /**
     * 获取随机数的编号,最小3位
     *
     * @return
     */
    public static String getRandomID(int len) {
        return RandomStringUtils.randomAlphabetic(3).toUpperCase() + RandomStringUtils.randomNumeric(len - 3);
    }

    /**
     * 获取随机状态数据, 0-3
     *
     * @return
     */
    public static int getRandomState() {
        return RandomUtils.nextInt(3);
    }

    /**
     * 获取随机状态数据, 0-n
     *
     * @return
     */
    public static int getRandomState(int n) {
        return RandomUtils.nextInt(n);
    }

    /**
     * 获取随机金额
     *
     * @return
     */
    public static BigDecimal getRandomMoney() {
        return new BigDecimal(RandomUtils.nextDouble() * 100000).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取随机金额
     *
     * @param scope 金额范围
     * @return
     */
    public static BigDecimal getRandomMoney(Long scope) {
        return new BigDecimal(RandomUtils.nextDouble() * scope).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getRandomMoney(Integer scope) {
        return getRandomMoney(scope.longValue());
    }

    /**
     * 获取随机日期
     *
     * @return
     */
    public static Date getRandomDate() {
        return getRandomDate("2007-01-01", DateUtil.formatCurrentDate());
    }

    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @return
     */
    public static Date getRandomDate(String beginDate) {
        return getRandomDate(beginDate, DateUtil.formatCurrentDate());
    }

    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @param endDate   结束日期，格式为：yyyy-MM-dd
     * @return
     */
    public static Date getRandomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = getRandomLong(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某一段的long值
     *
     * @param begin
     * @param end
     * @return
     */
    public static long getRandomLong(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return getRandomLong(begin, end);
        }
        return rtn;
    }

    /**
     * 获取某一段的long值
     * @param max   long的最大值
     * @return
     */
    public static long getRandomLong(Integer max) {
        return getRandomLong(1, max.longValue());
    }

    public static void main(String[] args) {
        System.out.println("getRandomID \t::::=> " + getRandomID());
        System.out.println("getRandomID \t::::=> " + getRandomID(12));
        System.out.println("getRandomState \t::::=> " + getRandomState());
        System.out.println("getRandomState \t::::=> " + getRandomState(5));
        System.out.println("getRandomMoney \t::::=> " + getRandomMoney());
        System.out.println("getRandomMoney \t::::=> " + getRandomMoney(500));
        System.out.println("getRandomDate \t::::=> " + getRandomDate().toLocaleString());
        System.out.println("getRandomDate \t::::=> " + getRandomDate("2015-01-01").toLocaleString());
        System.out.println("getRandomLong \t::::=> " + getRandomLong(10));
        System.out.println("getRandomLong \t::::=> " + getRandomLong(1, 200));
        System.out.println("getName \t::::=> " + ChineseNameUtil.getName());
    }

}
