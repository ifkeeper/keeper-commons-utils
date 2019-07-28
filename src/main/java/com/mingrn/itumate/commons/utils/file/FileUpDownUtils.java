package com.mingrn.itumate.commons.utils.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * If You want to download some files, And You can call {@link FileUpDownUtils#setDownloadResponseHeaders(HttpServletResponse, String)}
 * OR {@link FileUpDownUtils#setDownloadResponseHeaders(HttpServletRequest, HttpServletResponse, String)} methods.
 * Below is a sample Excel file download code:
 * <pre>{@code
 *    HttpServletRequest request;
 *    HttpServletResponse response;
 *    String fileName = "filename.ext";
 *
 *    FileUpDownloadUtils.setDownloadResponseHeaders(request, response, fileName);
 *    // If you have already set the file encoding mode, you can also use the following methods:
 *    // FileUpDownloadUtils.setDownloadResponseHeaders(response, fileName);
 *
 *    OutputStream out = response.getOutputStream();
 *
 *    // Class must Have write(Class<? extends OutputStream> out) methods.
 *    // e.g:  org.apache.poi.hssf.usermodel.HSSFWorkbook.write(OutputStream stream);
 *
 *    HSSFWorkbook workbook = new HSSWorkbook();
 *    // do something ...
 *
 *    workbook.write(out);
 *    out.flush();
 *    out.close();
 * }</pre>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/4/17 16:13
 */
public class FileUpDownUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUpDownUtils.class);
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String DEFAULT_FILE_PARAM = "uploadFile";

    private FileUpDownUtils() {
    }

    public static String encodeDownloadFileName(HttpServletRequest request, String fileName) {
        try {
            String agent = request.getHeader("USER-AGENT");
            if (StringUtils.isNotBlank(agent) && agent.contains("MSIE")) {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            } else if (StringUtils.isNotBlank(agent) && agent.contains("Mozilla")) {
                String encoding = request.getCharacterEncoding();
                fileName = new String(fileName.getBytes(encoding), StandardCharsets.ISO_8859_1);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encode Download File Name Exception {}", e);
        }

        return fileName;
    }

    public static void setDownloadResponseHeaders(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            String agent = request.getHeader("USER-AGENT");
            if (StringUtils.isNotBlank(agent)) {
                if (agent.contains("MSIE") || agent.contains("Trident")) {
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
                } else {
                    String encoding = request.getCharacterEncoding();
                    fileName = new String(fileName.getBytes(encoding), "ISO8859-1");
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Set Download Response Headers Exception {}", e);
        }
        setDownloadResponseHeaders(response, fileName);
    }

    public static void setDownloadResponseHeaders(HttpServletResponse response, String fileName) {
        String mimeType = MimeUtils.getFileMimeType(fileName);
        if (StringUtils.isNotBlank(mimeType)) {
            response.setContentType(mimeType);
        } else {
            LOGGER.warn("Undefined file type {}", fileName);
            mimeType = MimeUtils.getFileMimeType("txt.txt");
            response.setContentType(mimeType);
        }

        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setDateHeader("Expires", System.currentTimeMillis() + 1000L);
        response.setHeader("Pragma", "public");
    }


    public static void downloadFile(File file, HttpServletResponse response) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        } else {
            try (InputStream inputStream = new FileInputStream(file)) {
                int n;
                byte[] buffer = new byte[1024];
                while (-1 != (n = inputStream.read(buffer))) {
                    response.getOutputStream().write(buffer, 0, n);
                }
                response.getOutputStream().flush();
            } catch (Exception e) {
                LOGGER.error("Download file exception {}", e);
            }
        }
    }

    public static void uploadFileToDisk(HttpServletRequest request, String uploadFileSaveDir) throws IOException {
        File toPath = new File(uploadFileSaveDir);
        uploadFileToDisk(request, toPath);
    }

    public static void uploadFileToDisk(HttpServletRequest request, File uploadFileSaveDir) throws IOException {
        uploadFileToDisk(request, uploadFileSaveDir, DEFAULT_FILE_PARAM);
    }

    public static void uploadFileToDisk(HttpServletRequest request, File uploadFileSaveDir, String parameterName) throws IOException {
        FileWrap file = getUploadFile(request, parameterName);
        if (!uploadFileSaveDir.isDirectory() && !uploadFileSaveDir.mkdirs()) {
            throw new IOException("Failed create directory " + uploadFileSaveDir);
        } else {
            FileUtils.copyFile(file.getFile(), new File(uploadFileSaveDir + "/" + file.getName()));
        }
    }

    public static byte[] getFileContent(File file) throws Exception {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream out = new ByteArrayOutputStream(1024)) {
            int size;
            byte[] temp = new byte[1024];
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Get File Byte Content Exception {}", e);
        }
        return null;
    }

    public static FileWrap getUploadFile(HttpServletRequest request) throws IOException {
        return getUploadFile(request, DEFAULT_FILE_PARAM);
    }

    public static FileWrap getUploadFile(HttpServletRequest request, String parameterName) throws IOException {
        MultipartHttpServletRequest multipartRequest;
        FileWrap uploadFile = null;

        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch (ClassCastException e) {
            LOGGER.error("Get Upload File Exception {}", e);
            return null;
        }

        MultipartFile multipartFile = multipartRequest.getFile(parameterName);
        if (multipartFile != null && StringUtils.isNotBlank(multipartFile.getOriginalFilename())) {
            String fileName = multipartFile.getOriginalFilename();
            String fileRealPath = TMP_DIR + File.separator + getTempFileName(fileName);
            File file = new File(fileRealPath);
            file.delete();
            multipartFile.transferTo(file);
            uploadFile = new FileWrap(fileName, file);
        }

        return uploadFile;
    }

    public static List<FileWrap> getUploadFiles(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest;
        ArrayList<FileWrap> result = new ArrayList<>();
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
            Iterator it = multipartRequest.getFileNames();

            while (it.hasNext()) {
                String key = (String) it.next();
                MultipartFile multipartFile = multipartRequest.getFile(key);
                if (multipartFile != null && StringUtils.isNotBlank(multipartFile.getOriginalFilename())) {
                    String fileName = multipartFile.getOriginalFilename();
                    String fileRealPath = TMP_DIR + File.separator + getTempFileName(fileName);
                    File file = new File(fileRealPath);
                    file.delete();
                    multipartFile.transferTo(file);
                    result.add(new FileWrap(fileName, file));
                }
            }
        } catch (ClassCastException e) {
            LOGGER.error("Get files exception {}", e);
        }

        return result;
    }

    public static String getTempFileName(String original) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(new Date()) + "_" + original;
    }

    public static class MimeUtils {
        private static Properties properties;
        public static final String MIME_TYPES_PROPERTIES = "mimeTypes.properties";

        static {
            try {
                properties = new Properties();
                properties.load((new MimeUtils()).getClass().getResourceAsStream(MIME_TYPES_PROPERTIES));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public MimeUtils() {
        }

        public static String getFileMimeType(File file) {
            return file == null ? null : getFileMimeType(file.getName());
        }

        public static String getFileMimeType(String fileName) {
            if (!StringUtils.isBlank(fileName) && fileName.contains(".")) {
                fileName = fileName.substring(fileName.lastIndexOf("."));
                return getExtensionMimeType(fileName);
            } else {
                return null;
            }
        }

        public static String getExtensionMimeType(String extension) {
            String result = null;
            if (StringUtils.isNotBlank(extension)) {
                extension = extension.toLowerCase();
                if (!extension.startsWith(".")) {
                    extension = "." + extension;
                }
                result = (String) properties.get(extension);
            }
            return result;
        }
    }
}
