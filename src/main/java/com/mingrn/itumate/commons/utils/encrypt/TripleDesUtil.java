package com.mingrn.itumate.commons.utils.encrypt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * DESede 加密
 * <p>
 * DESede是由DES对称加密算法改进后的一种对称加密算法.使用 168 位的密钥对资料进行三次加密的一种机制;
 * 它通常(但非始终)提供极其强大的安全性.如果三个 56 位的子元素都相同,则三重 DES 向后兼容 DES.
 * <p>
 * DESede 加密算法加密密码为 8 的倍数, 否则会抛出 {@link java.security.InvalidKeyException Wrong key size} 异常.
 * <p>
 * {@link #encryptMode(String, byte[])} 方法为数据加密算法, 加密密码应为 24 位, 加密数据为 <code>byte[]</code>
 * 类型, 推荐使用 {@link StandardCharsets#UTF_8}.示例如下:
 * <pre>{@code
 *   // 加密密码, 24 位字符串
 *   String pwd = "...";
 *
 *   // 这里加密数据为字符类型
 *   String data = "...";
 *   // 转 byte[]
 *   byte[] d = data.toByte(StandardCharsets.UTF_8);
 *
 *   TripleDesUtil.encryptMode(pwd, d);
 * }</pre>
 * 注意, 该加密算法低位使用了 <span>:</span>, 在解密时应将该符号剔除.
 * <p>
 * {@link #decryptMode(String, String)} 方法为数据解密算法, 解密密码应为 24 位, 解密数据为 <code>String</code>
 * 类型.示例如下:
 * <pre>{@code
 *   // 解密密码, 24 位字符串
 *   String pwd = "...";
 *
 *   // 解密数据
 *   String data = "...";
 *
 *   TripleDesUtil.decryptMode(pwd, data);
 * }</pre>
 * <p>
 * 完整使用示例:
 * <pre>{@code
 *   //-----------------------数据加密------------------------------
 *
 *   // 通过密码工具类生成 24 位密码
 *   String pwd = SecurePasswordGenerator.INSTANCE.generate(24);
 *
 *   // 使用 ID 生成工具类生成一个 uId
 *   String uId = GeneratorIDFactory.generatorUserId();
 *
 *   // 加密
 *   String encrypt = TripleDesUtil.encryptMode(pwd, uId.getBytes(StandardCharsets.UTF_8));
 *
 *   //-----------------------数据解密------------------------------
 *
 *   // 解密密码为加密密码
 *   // 解密内容为加密内容
 *
 *   String decrypt = TripleDesUtil.decryptMode(pwd, encrypt);
 * }</pre>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-07-30 10:37
 */
public final class TripleDesUtil {

    private TripleDesUtil() {}

    /**
     * 定义加密算法,可用 DES,DESede,Blowfish
     */
    private static final String ENCRYPT_ALGORITHM = "DESede";

    private static final String HEX_CHARS = "0123456789ABCDEF";

    private static final Logger LOGGER = LoggerFactory.getLogger(TripleDesUtil.class);

    /**
     * 加密算法
     * <p>
     * 该加密算法只能通过 {@link #decryptMode(String, String)} 进行解密.
     * 使用示例如下:
     * <pre>{@code
     *   String pwd = "...";
     *   String data = "...";
     *
     *   String encrypt = TripleDesUtil.encryptMode(pwd, data.getByte(StandardCharsets.UTF_8);
     * }</pre>
     *
     * @param password 加密密钥, 密码为8的倍数,请使用 24 位.
     * @param data     被加密的数据
     */
    public static String encryptMode(String password, byte[] data) {
        //生成密钥
        SecretKey desKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ENCRYPT_ALGORITHM);
        //加密
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            // 这里使用特定转16进制算法,不能在外部使用, 解码需要使用 hex2byte 方法
            return byte2hex(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密算法
     * <p>
     * 该解密算法只能解 {@link #encryptMode(String, byte[])} 的加密数据.
     * 使用示例:
     * <pre>{@code
     *   String pwd = "...";
     *   String data = "...";
     *
     *   String decrypt = TripleDesUtil.decryptMode(pwd, data);
     * }</pre>
     *
     * @param password 加密密钥, 密码为8的倍数,请使用 24 位.
     * @param data     被加密的数据
     */
    public static String decryptMode(String password, String data) {
        try {
            if (StringUtils.isBlank(data)) {
                return null;
            }
            //生成密钥
            SecretKey desKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ENCRYPT_ALGORITHM);
            //解密
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            // 替换 :
            return new String(cipher.doFinal(Objects.requireNonNull(hex2byte(data.replace(":", "")))));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 二进制转换成十六进制字符串
     *
     * @param b 数据
     * @return 十六进制数据
     */
    private static String byte2hex(byte[] b) {
        String tmp;
        StringBuilder builder = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                builder.append("0").append(tmp);
            } else {
                builder.append(tmp);
            }
            // 补 :
            if (n < b.length - 1) {
                builder.append(":");
            }
        }
        return builder.toString().toUpperCase();
    }

    /**
     * 十六进制转二进制
     *
     * @param hexStr 十六进制字符串
     * @return byte[]
     */
    private static byte[] hex2byte(String hexStr) {
        if (StringUtils.isBlank(hexStr)) {
            return null;
        }
        hexStr = hexStr.toUpperCase();
        int length = hexStr.length() / 2;
        char[] hexChars = hexStr.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) HEX_CHARS.indexOf(c);
    }
}