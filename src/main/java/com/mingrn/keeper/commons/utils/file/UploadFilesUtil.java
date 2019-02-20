package com.mingrn.keeper.commons.utils.file;

import com.mingrn.keeper.commons.utils.enums.FileTypeEnums;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 文件上传工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-01-28 11:19
 */
public final class UploadFilesUtil {

    private UploadFilesUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFilesUtil.class);

    /**
     * 文件媒体类型
     */
    /*private static final Map<FileTypeEnums, String> FILE_TYPE_MAP = new HashMap<>(4);

    static {
        FILE_TYPE_MAP.put(FileTypeEnums.FLASH, "swf,flv");
        FILE_TYPE_MAP.put(FileTypeEnums.IMAGE, "gif,jpg,jpeg,png,bmp");
        FILE_TYPE_MAP.put(FileTypeEnums.MEDIA, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        FILE_TYPE_MAP.put(FileTypeEnums.FILE, "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
    }*/

    /**
     * 文件上传
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     */
    public static void upload(HttpServletRequest request, FileTypeEnums fileType) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        while (iterator.hasNext()) {
            String original = iterator.next();
            MultipartFile multipartFile = multipartHttpServletRequest.getFile(original);
            if (multipartFile == null || multipartFile.isEmpty()) {
                continue;
            }
            try {
                String originalFileName = multipartFile.getOriginalFilename();
                if (StringUtils.isBlank(originalFileName) || !originalFileName.contains(".")) {
                    // TODO: 2019-01-28 非法文件
                }
                String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList(FileTypeEnums.FILE_TYPE_MAP.get(fileType).split(",")).contains(ext)) {
                    // TODO: 2019-01-28 非法文件
                }
                byte[] data = multipartFile.getBytes();
            } catch (IOException e) {
                LOGGER.error("Get file bytes exception", e);
            }
        }
    }
}
