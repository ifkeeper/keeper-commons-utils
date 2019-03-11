package com.mingrn.itumate.commons.utils.enums;

import com.mingrn.itumate.commons.utils.constants.DateTimeConstant;

/**
 * 日期枚举类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-01-08 23:28
 */
public enum DateTimeEnums {
    /**
     * yyyy-MM
     */
    YYYY_MM(DateTimeConstant.YYYY_MM),
    /**
     * yyyy-MM-dd
     */
    YYYY_MM_DD(DateTimeConstant.YYYY_MM_DD),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS(DateTimeConstant.YYYY_MM_DD_HH_MM_SS),
    /**
     * yyyy-MM-dd HH:mm:ss ss
     */
    YYYY_MM_DD_HH_MM_SS_SS(DateTimeConstant.YYYY_MM_DD_HH_MM_SS_SS);

    private String formatStr;

    private DateTimeEnums(String formatStr) {
        this.formatStr = formatStr;
    }

    public String getFormatStr() {
        return formatStr;
    }
}
