package com.mingrn.itumate.commons.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    /** UTF-8 */
    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    /**
     * base64加密
     *
     * @param txt 明文文本
     * @return String 密文文本
     */
    public static String encryption(String txt) {
        byte[] encByte = BASE_64.encode(txt.getBytes(UTF8_CHARSET));
        return new String(encByte, UTF8_CHARSET);
    }

    /**
     * base64加密
     *
     * @param data 明文数据
     * @return String 密文文本
     */
    public static String encryption(byte[] data) {
        byte[] encByte = BASE_64.encode(data);
        return new String(encByte, UTF8_CHARSET);
    }

    /**
     * base64解密
     *
     * @param enc 密文文本
     * @return String 明文文本
     */
    public static String decrypt(String enc) {
        byte[] srcByte = BASE_64.decode(enc.getBytes(UTF8_CHARSET));
        return new String(srcByte, UTF8_CHARSET);
    }

    public static byte[] decryptByte(String enc) {
        byte[] encByte = enc.getBytes(UTF8_CHARSET);
        return BASE_64.decode(encByte);
    }

    public static String decryptByte(byte[] enc) {
        byte[] srcByte = BASE_64.decode(enc);
        return new String(srcByte, UTF8_CHARSET);
    }
}