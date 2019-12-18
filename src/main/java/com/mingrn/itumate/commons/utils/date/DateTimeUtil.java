package com.mingrn.itumate.commons.utils.date;

import com.mingrn.itumate.commons.utils.enums.DateTimeEnums;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Objects;

/**
 * 日期工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-01-08 23:24
 */
public class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * 当前时间 yyyy-MM-dd
     *
     * @return Date
     */
    public static Date nowDate() {
        return LocalDate.now().toDate();
    }

    /**
     * 当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return Datetime
     */
    public static Date nowDatetime() {
        return new DateTime().toDate();
    }

    /**
     * 当前日期所处星期的第一天
     *
     * @return Date
     */
    public static Date curWeekFirstDay(){
        LocalDate localDate = LocalDate.now().dayOfWeek().withMinimumValue();
        return localDate.toDate();
    }

    /**
     * 指定日期所处星期的第一天
     *
     * @return Date
     */
    public static Date curWeekFirstDay(Date target){
        Objects.requireNonNull(target, "target Date Can't be null");
        LocalDate localDate = new LocalDate(target).dayOfWeek().withMinimumValue();
        return localDate.toDate();
    }

    /**
     * 当前日期所处星期的最后一天
     *
     * @return Date
     */
    public static Date curWeekLastDay(){
        LocalDate localDate = LocalDate.now().dayOfWeek().withMaximumValue();
        return localDate.toDate();
    }

    /**
     * 指定日期所处星期的最后一天
     *
     * @return Date
     */
    public static Date curWeekLastDay(Date target){
        Objects.requireNonNull(target, "target Date Can't be null");
        LocalDate localDate = new LocalDate(target).dayOfWeek().withMaximumValue();
        return localDate.toDate();
    }

    /**
     * 当前日期所处月份的第一天
     *
     * @return Date
     */
    public static Date curMonthFirstDay(){
        LocalDate localDate = LocalDate.now().dayOfMonth().withMinimumValue();
        return localDate.toDate();
    }

    /**
     * 指定日期所处月份的第一天
     *
     * @return Date
     */
    public static Date curMonthFirstDay(Date target){
        Objects.requireNonNull(target, "target Date Can't be null");
        LocalDate localDate = new LocalDate(target).dayOfMonth().withMinimumValue();
        return localDate.toDate();
    }

    /**
     * 当前日期所处月份的最后一天
     *
     * @return Date
     */
    public static Date curMonthLastDay(){
        LocalDate localDate = LocalDate.now().dayOfMonth().withMaximumValue();
        return localDate.toDate();
    }

    /**
     * 指定日期所处月份的最后一天
     *
     * @return Date
     */
    public static Date curMonthLastDay(Date target){
        Objects.requireNonNull(target, "target Date Can't be null");
        LocalDate localDate = new LocalDate(target).dayOfMonth().withMaximumValue();
        return localDate.toDate();
    }

    /**
     * 将string字符串转化为Date类型的字符串
     *
     * @param dateTimeStr 需要转化的string类型的时间
     * @return 返回转化后的Date类型的时间
     */
    public static Date str2Date(String dateTimeStr) {
        return str2Date(dateTimeStr, DateTimeEnums.YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 将string字符串转化为Date类型的字符串
     *
     * @param dateTimeStr 需要转化的string类型的字符串
     * @param enums       转化规则 {@link DateTimeEnums}
     * @return 返回转化后的Date类型的时间
     */
    public static Date str2Date(String dateTimeStr, DateTimeEnums enums) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(enums.getFormatStr());
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }


    /**
     * 将date类型的时间转化为string类型
     *
     * @param date 需要转化的date类型的时间
     * @return 返回转化后的string类型的时间
     */
    public static String date2Str(Date date) {
        return date2Str(date, DateTimeEnums.YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 将date类型的时间转化为string类型
     *
     * @param date  需要转化的date类型的时间
     * @param enums 转化规则 {@link DateTimeEnums}
     * @return 返回转化后的string类型的时间
     */
    public static String date2Str(Date date, DateTimeEnums enums) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(enums.getFormatStr());
    }


    /**
     * 时间大小比较
     * <p>
     * date1 大于 date2 返回 1
     * date1 小于 date2 返回 -1
     * data1 等于 date2 返回 0
     *
     * @param target 目标时间
     * @param refer  参考时间
     * @return date1 > date2
     */
    public static boolean compare(Date target, Date refer) {
        DateTime dateTime1 = new DateTime(target);
        DateTime dateTime2 = new DateTime(refer);
        return dateTime1.compareTo(dateTime2) > 0;
    }

    /**
     * 时间大小比较
     * <p>
     * date1 大于 date2 返回 1
     * date1 小于 date2 返回 -1
     * data1 等于 date2 返回 0
     *
     * @param target 目标时间
     * @param refer  参考时间
     * @return date1 > date2
     */
    public static boolean compare(String target, String refer) {
        DateTime dateTime1 = new DateTime(target);
        DateTime dateTime2 = new DateTime(refer);
        return dateTime1.compareTo(dateTime2) > 0;
    }
}