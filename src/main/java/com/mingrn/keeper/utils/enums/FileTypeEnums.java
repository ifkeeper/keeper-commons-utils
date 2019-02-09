package com.mingrn.keeper.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型枚举类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-01-28 13:36
 */
public enum FileTypeEnums {
    /**
     * flash
     */
    FLASH,
    /**
     * 图片
     */
    IMAGE,
    /**
     * 媒体
     */
    MEDIA,
    /**
     * 其他文件
     */
    FILE;

    /**
     * 文件媒体类型
     */
    public static final Map<FileTypeEnums, String> FILE_TYPE_MAP = new HashMap<>(4);

    static {
        FILE_TYPE_MAP.put(FileTypeEnums.FLASH, "swf,flv");
        FILE_TYPE_MAP.put(FileTypeEnums.IMAGE, "gif,jpg,jpeg,png,bmp");
        FILE_TYPE_MAP.put(FileTypeEnums.MEDIA, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        FILE_TYPE_MAP.put(FileTypeEnums.FILE, "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
    }
}