package com.mingrn.keeper.commons.utils.zip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip 工具类
 *
 * @author MinGRn
 */
@SuppressWarnings("unchecked")
public final class GZipUtil {

    private GZipUtil() {
    }

    private static final int BUFFER_SIZE = 1024;
    private static final String GZIP_EXT = ".gz";

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipUtil.class);


    /**
     * 数据压缩
     *
     * @param data 压缩数据
     */
    public static byte[] compress(byte[] data) {
        byte[] output = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            compress(inputStream, outputStream);
            output = outputStream.toByteArray();
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("data compress exception", e);
        }
        return output;
    }


    /**
     * 文件压缩
     *
     * @param file 文件
     */
    public static void compress(File file) throws Exception {
        compress(file, true);
    }


    /**
     * 文件压缩
     *
     * @param path 文件路径
     */
    public static void compress(String path) {
        compress(path, true);
    }


    /**
     * 文件压缩
     *
     * @param path   文件路径
     * @param delete 是否删除原始文件
     */
    public static void compress(String path, boolean delete) {
        File file = new File(path);
        compress(file, delete);
    }


    /**
     * 文件压缩
     *
     * @param file   压缩文件
     * @param delete 是否删除原始文件
     */
    public static void compress(File file, boolean delete) {
        try (FileInputStream inputStream = new FileInputStream(file);
             FileOutputStream outputStream = new FileOutputStream(file.getPath() + GZIP_EXT)) {
            compress(inputStream, outputStream);
            outputStream.flush();
            if (delete) {
                file.deleteOnExit();
            }
        } catch (IOException e) {
            LOGGER.error("data compress exception", e);
        }
    }


    /**
     * 数据压缩
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    public static void compress(InputStream inputStream, OutputStream outputStream) {
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                gzip.write(data, 0, count);
            }
            gzip.finish();
            gzip.flush();
        } catch (IOException e) {
            LOGGER.error("data compress exception", e);
        }
    }


    /**
     * 数据解压数据
     *
     * @param data 压缩数据
     * @return
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            decompress(inputStream, outputStream);
            data = outputStream.toByteArray();
            outputStream.flush();
            return data;
        } catch (IOException e) {
            LOGGER.error("data decompress exception", e);
        }
        return null;
    }


    /**
     * 数据解压文件
     *
     * @param inputStream  数据输入流
     * @param outputStream 数据输出流
     */
    public static void decompress(InputStream inputStream, OutputStream outputStream) {
        try (GZIPInputStream gzip = new GZIPInputStream(inputStream)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = gzip.read(data, 0, BUFFER_SIZE)) != -1) {
                outputStream.write(data, 0, count);
            }
        } catch (IOException e) {
            LOGGER.error("data decompress exception", e);
        }
    }
}