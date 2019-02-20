package com.mingrn.keeper.commons.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * base64加解密工具类
 *
 * @author MinGR
 */
public final class Base64Util {

	private Base64Util() {
	}

	/**
	 * base64
	 */
	private static final Base64 BASE_64 = new Base64();

	/**
	 * base64加密
	 *
	 * @param txt 明文文本
	 * @param charset 编码格式
	 * @return String 密文文本
	 */
	public static String encryption(String txt, String charset) throws Exception {
		byte[] srcByte = txt.getBytes(charset);
		byte[] encByte = BASE_64.encode(srcByte);
		return new String(encByte, charset);
	}

	public static String encryption(byte[] data, String charset) throws Exception {
		byte[] encByte = BASE_64.encode(data);
		return new String(encByte, charset);
	}

	/**
	 * base64解密
	 *
	 * @param enc 密文文本
	 * @param charset 编码格式
	 * @return String 明文文本
	 */
	public static String decrypt(String enc, String charset) throws Exception {
		byte[] encByte = enc.getBytes(charset);
		byte[] srcByte = BASE_64.decode(encByte);
		return new String(srcByte, charset);
	}

	public static byte[] decryptByte(String enc, String charset) throws Exception {
		byte[] encByte = enc.getBytes(charset);
		return BASE_64.decode(encByte);
	}

	public static String decryptByte(byte[] enc, String charset) throws Exception {
		byte[] srcByte = BASE_64.decode(enc);
		return new String(srcByte, charset);
	}

	public static byte[] encryptByte(String src, String charset) throws Exception {
		byte[] srcByte = src.getBytes(charset);
		return BASE_64.encode(srcByte);
	}

	public static String md5(String src) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] input = src.getBytes();
		byte[] buff = md.digest(input);
		return bytesToHex(buff);
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder md5Str = new StringBuilder();
		int digital;
		for (byte aByte : bytes) {
			digital = aByte;
			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5Str.append("0");
			}
			md5Str.append(Integer.toHexString(digital));
		}
		return md5Str.toString().toUpperCase();

	}
}