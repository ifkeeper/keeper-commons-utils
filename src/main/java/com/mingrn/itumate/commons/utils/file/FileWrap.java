package com.mingrn.itumate.commons.utils.file;

import java.io.File;
import java.io.Serializable;

/**
 * 文件包装
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/4/18 10:38
 */
public class FileWrap implements Serializable {

    private static final long serialVersionUID = -607514736746905893L;

    /** 文件名称 */
    private String name;

    /** 文件 */
    private File file;

    /** 文件大小 */
    private Long size;

    /** 文件是否存在*/
    private Boolean exists;

    public FileWrap(File file) {
        this(file.getName(), file);
    }

    public FileWrap(String name, File file) {
        this.name = name;
        this.file = file;
        this.size = this.file.length();
        this.exists = this.file.exists();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getSize() {
        return size;
    }

    public Boolean getExists() {
        return exists;
    }
}