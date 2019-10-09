package com.mingrn.itumate.commons.utils.img;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.*;

/**
 * 图片转化base64后再UrlEncode结果
 *
 * @author MinGR
 */
public class BaseImg64Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseImg64Util.class);

	/**
	 * 将一张本地磁盘图片转化成Base64字符串
	 *
	 * @param imgPath 本地图片地址
	 * @return 图片转化base64后再UrlEncode结果
	 */
	public static String getBaseImage64StrFromDisk(String imgPath) {
		try (InputStream inputStream = new FileInputStream(imgPath)) {
			return inputIOTransform2Base64(inputStream);
		} catch (IOException e) {
			LOGGER.error("image file io load err >>>>", e);
		}
		return null;
	}


	/**
	 * 将一张网络图片转化成Base64字符串
	 *
	 * @param url 网络图片地址
	 * @return 图片转化base64后再UrlEncode结果
	 */
	public static String getBaseImage64StrFromNet(String url) {

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

			HttpUriRequest uriRequest = RequestBuilder.get(url).build();
			InputStream inputStream = httpClient.execute(uriRequest).getEntity().getContent();

			int len;
			byte[] buffer = new byte[1024];
			while ((len = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
			byte[] data = byteArrayOutputStream.toByteArray();
			return URLEncoder.encode(new BASE64Encoder().encode(data), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("read image io from net err >>>>", e);
		}
		return null;
	}


	/**
	 * Input IO 转 Base64 Str(URLEncoder)
	 */
	public static String inputIOTransform2Base64(InputStream inputStream) {
		// 读取图片字节数组
		byte[] data = null;
		try {
			data = new byte[inputStream.available()];
			inputStream.read(data);
		} catch (IOException e) {
			LOGGER.error("Image load stream err", e);
		}

		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过再URLEncode的字节数组字符串
		try {
			assert data != null;
			return URLEncoder.encode(encoder.encode(data), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}