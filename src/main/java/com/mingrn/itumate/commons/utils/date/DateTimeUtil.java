package com.mingrn.itumate.commons.utils.date;

import com.mingrn.itumate.commons.utils.enums.DateTimeEnums;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

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
     * 将string字符串转化为Date类型的字符串
     *
     * @param dateTimeStr 需要转化的string类型的时间
     * @return 返回转化后的Date类型的时间
     */
    public static Date strToDate(String dateTimeStr) {
        return strToDate(dateTimeStr, DateTimeEnums.YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 将string字符串转化为Date类型的字符串
     *
     * @param dateTimeStr 需要转化的string类型的字符串
     * @param enums       转化规则 {@link DateTimeEnums}
     * @return 返回转化后的Date类型的时间
     */
    public static Date strToDate(String dateTimeStr, DateTimeEnums enums) {
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
    public static String dateToStr(Date date) {
        return dateToStr(date, DateTimeEnums.YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 将date类型的时间转化为string类型
     *
     * @param date  需要转化的date类型的时间
     * @param enums 转化规则 {@link DateTimeEnums}
     * @return 返回转化后的string类型的时间
     */
    public static String dateToStr(Date date, DateTimeEnums enums) {
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
}