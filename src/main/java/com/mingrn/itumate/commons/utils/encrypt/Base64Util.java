package com.mingrn.itumate.commons.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * base64加解密工具类
 *
 * @author MinGR
 */
public final class Base64Util {

    private Base64Util() {
    }

    /** base64 */
    private static final Base64 BASE_64 = new Base64();

    /**
     * base64加密
     *
     * @param txt     明文文本
     * @param charset 编码格式
     * @return String 密文文本
     */
    public static String encryption(String txt, Charset charset) {
        byte[] srcByte = txt.getBytes(charset);
        byte[] encByte = BASE_64.encode(srcByte);
        return new String(encByte, charset);
    }

    /**
     * base64加密
     *
     * @param data    明文数据
     * @param charset 编码格式
     * @return String 密文文本
     */
    public static String encryption(byte[] data, Charset charset) {
        byte[] encByte = BASE_64.encode(data);
        return new String(encByte, charset);
    }

    /**
     * base64解密
     *
     * @param enc     密文文本
     * @param charset 编码格式
     * @return String 明文文本
     */
    public static String decrypt(String enc, Charset charset) {
        byte[] encByte = enc.getBytes(charset);
        byte[] srcByte = BASE_64.decode(encByte);
        return new String(srcByte, charset);
    }

    public static byte[] decryptByte(String enc, Charset charset) {
        byte[] encByte = enc.getBytes(charset);
        return BASE_64.decode(encByte);
    }

    public static String decryptByte(byte[] enc, Charset charset) {
        byte[] srcByte = BASE_64.decode(enc);
        return new String(srcByte, charset);
    }

    /**
     * base64加密
     *
     * @param src     加密文本
     * @param charset 编码格式
     * @return 加密数据
     */
    public static byte[] encryptByte(String src, Charset charset) {
        byte[] srcByte = src.getBytes(charset);
        return BASE_64.encode(srcByte);
    }

    /**
     * md5 加密
     *
     * @param src 加密文本
     * @return 加密数据
     */
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