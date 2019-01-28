package com.mingrn.keeper.utils.zip;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip 压缩工具类
 *
 * @author MinGRn
 */
@SuppressWarnings("unchecked")
public final class GZipUtil {

	private GZipUtil() {
	}

	private static final int BUFFER = 1024;
	private static final String EXT = ".gz";


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
			// TODO: 24/12/2018 IOException handler ...
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
	 * @param path 文件路径
	 * @param delete 是否删除原始文件
	 */
	public static void compress(String path, boolean delete) {
		File file = new File(path);
		compress(file, delete);
	}


	/**
	 * 文件压缩
	 *
	 * @param file 压缩文件
	 * @param delete 是否删除原始文件
	 */
	public static void compress(File file, boolean delete) {
		try (FileInputStream inputStream = new FileInputStream(file);
			 FileOutputStream outputStream = new FileOutputStream(file.getPath() + EXT)) {
			compress(inputStream, outputStream);
			outputStream.flush();
			if (delete) {
				file.deleteOnExit();
			}
		} catch (IOException e) {
			// TODO: 24/12/2018 File IO Exception handler ...
		}
	}


	/**
	 * 数据压缩
	 *
	 * @param inputStream 输入流
	 * @param outputStream 输出流
	 */
	public static void compress(InputStream inputStream, OutputStream outputStream) {
		try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
				gzip.write(data, 0, count);
			}
			gzip.finish();
			gzip.flush();
		} catch (IOException e) {
			// TODO: 24/12/2018 GZip IO Exception handler ...
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
			// TODO: 24/12/2018 IOException handler ...
		}
		return null;
	}


	/**
	 * 数据解压文件
	 *
	 * @param inputStream 数据输入流
	 * @param outputStream 数据输出流
	 */
	public static void decompress(InputStream inputStream, OutputStream outputStream) {
		try (GZIPInputStream gis = new GZIPInputStream(inputStream)) {
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = gis.read(data, 0, BUFFER)) != -1) {
				outputStream.write(data, 0, count);
			}
			gis.close();
		} catch (IOException e) {
			// TODO: 24/12/2018 IOException handler ...
		}
	}
}