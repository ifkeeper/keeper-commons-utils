package com.mingrn.keeper.utils;

import com.mingrn.keeper.utils.date.DateTimeUtil;
import com.mingrn.keeper.utils.enums.DateTimeEnums;

import java.util.Date;

/**
 * 测试主类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-01-08 23:03
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(DateTimeUtil.dateToStr(new Date(), DateTimeEnums.YYYY_MM_DD_HH_MM_SS_SS));
        System.out.println(DateTimeUtil.strToDate("2019-01-08 23:37:57 57", DateTimeEnums.YYYY_MM_DD_HH_MM_SS_SS));
    }
}
