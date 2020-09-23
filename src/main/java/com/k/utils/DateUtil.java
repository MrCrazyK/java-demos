package com.k.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @Description: 时间工具类
 * @Author 王一鸣
 * @Date 2019/8/8
 */
public class DateUtil {

    /**
     * 日志类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private static final SimpleDateFormat FULL_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat FULL_DAY = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date date
     * @return string
     */
    public static String formatDateToSecond(Date date) {
        synchronized (FULL_SECOND) {
            return FULL_SECOND.format(date);
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param strDate date
     * @return Date
     */
    public static Date parseSecond(String strDate) throws ParseException {
        synchronized (FULL_SECOND) {
            return FULL_SECOND.parse(strDate);
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param strDate date
     * @return Date
     */
    public static Date parseSecondDefaultNull(String strDate) {
        synchronized (FULL_SECOND) {
            Date res;
            try {
                res = FULL_SECOND.parse(strDate);
            } catch (ParseException e) {
                res = null;
            }
            return res;
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date date
     * @return string
     */
    public static String formatDateToDate(Date date) {
        synchronized (FULL_DAY) {
            return FULL_DAY.format(date);
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param strDate date
     * @return Date
     */
    public static Date parseDate(String strDate) throws ParseException {
        synchronized (FULL_DAY) {
            return FULL_DAY.parse(strDate);
        }
    }

    /**
     * 获取前三个月时间("年份-月份"的形式,月份不含0)包含当前月
     *
     * @return 月份列表
     */
    public static List<String> getThreeTime() {
        ArrayList<String> res = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        //暂时改成12个月
        for (int i = 0; i < 12; i++) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            res.add(year + "-" + month);
            calendar.add(Calendar.MONTH, -1);
        }
        return res;
    }

    /**
     * 判断当前日期是否为月末
     *
     * @return boolean
     */
    public static boolean isLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前日期是否为月末
     *
     * @return boolean
     */
    public static boolean isLastDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        // 获取当前月份
        int month = calendar.get(Calendar.MONTH) + 1;
        // 判断当前为12月份，如果是12月份在判断是否为最后一天
        if (month == 12) {
            calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
            return calendar.get(Calendar.DAY_OF_MONTH) == 1;
        }
        return false;
    }

    /**
     * 获取下一年份
     *
     * @return int
     */
    public static int getNextYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        return year + 1;
    }

    /**
     * 得到xxx分钟后的时间
     *
     * @param minute 分钟数
     * @return Date
     * @throws java.text.ParseException 转换异常
     */
    public static Date getAnyTimeAfter(int minute) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
    }

    /**
     * @param str 转换的时间
     * @param sdf 转换的时间格式
     * @return Date
     * @Description: 字符串转时间
     * @author zhangmax
     * @date 2019年6月1日下午8:31:01
     */
    public static Date strDate(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return new java.sql.Date(date.getTime());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    /**
     * @return String
     * @Description: 返回时间带-时间格式
     * @author zhangmax
     * @date 2019年6月2日下午6:27:35
     */
    public static String getNowTimeTwo() {
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = df.format(new Date());
        return nowTime;
    }

    /**
     * 获得当前时间的年月
     *
     * @return String
     */
    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(new Date());
    }

    /**
     * 获取下个月（格式：yyyyMM）
     *
     * @return String
     */
    public static String getNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * @return Date
     * @Description: 当天的开始时间
     * @author Administrator
     * @date 2019年8月11日下午3:53:48
     */
    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * @return Date
     * @Description: 当天的结束时间
     * @author Administrator
     * @date 2019年8月11日下午3:53:48
     */
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * @return Date
     * @Description: 昨天的开始时间
     * @author Administrator
     * @date 2019年8月11日下午3:53:48
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * @return Date
     * @Description: 昨天的结束时间
     * @author Administrator
     * @date 2019年8月11日下午3:53:48
     */
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * @return Date
     * @Description: 昨天的当前时间
     * @author Administrator
     * @date 2019年8月11日下午3:53:48
     */
    public static Date getTimeOfYesterDay() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1);
        return ca.getTime();
    }

    /**
     * @param date 当前时间
     * @param days 几天前或几天后，若是几天前，则为负值
     * @return java.util.Date
     * @Description: 日期加减
     * @author zhangmax
     * @date 2019/8/13 20:11
     */
    public static Date calculateCurrentDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date result = calendar.getTime();
        return stringToDate(dateToString(result, "yyyy-MM-dd"));
    }

    /**
     * 将日期Date格式转换为字符串格式
     *
     * @param data   时间
     * @param format 格式化类型
     * @return String
     */
    public static String dateToString(Date data, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        return sformat.format(data);
    }

    /**
     * 将日期字符串格式转换为Date格式
     *
     * @param dateString 字符串格式时间
     * @return Date
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }

    /**
     * @param a 为负数时-1即为1天前
     * @param b b
     * @return 时间区间list
     * @Description:日期的时间区间的获取，a天前的00:00 - b天前的23:59:59
     */
    public List<String> getTimeInterval(int a, int b) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(new Date());
        cal2.setTime(new Date());

        // 时间区间为a天前的00:00 - b天前的23:59:59
        cal1.add(Calendar.DAY_OF_MONTH, a);
        cal2.add(Calendar.DAY_OF_MONTH, b);

        List<String> list = new ArrayList();

        String startTime = sdf1.format(cal1.getTime());
        String endTime = sdf2.format(cal2.getTime());
        list.add(startTime);
        list.add(endTime);

        return list;
    }

}
