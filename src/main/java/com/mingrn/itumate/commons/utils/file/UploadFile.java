package com.mingrn.itumate.commons.utils.file;

import java.io.File;
import java.io.Serializable;

/**
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/4/18 10:38
 */
public class UploadFile implements Serializable {

    private static final long serialVersionUID = -607514736746905893L;
    private String fileName;
    private File file;

    UploadFile() {
    }

    UploadFile(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}