package com.mingrn.itumate.commons.utils.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;
import java.util.UUID;


/**
 * DESede 加密
 * <p>
 * DESede是由DES对称加密算法改进后的一种对称加密算法。使用 168 位的密钥对资料进行三次加密的一种机制;
 * 它通常(但非始终)提供极其强大的安全性。如果三个 56 位的子元素都相同,则三重 DES 向后兼容 DES。
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-07-30 10:37
 */
public final class TripleDESUtil {

    private TripleDESUtil() {
    }

    /**
     * 定义加密算法,可用 DES,DESede,Blowfish
     */
    private static final String ENCRYPT_ALGORITHM = "DESede";

    /**
     * 加密算法
     *
     * @param password 加密密钥
     * @param data     被加密的数据
     */
    public static String encryptMode(String password, byte[] data) {
        //生成密钥
        SecretKey desKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ENCRYPT_ALGORITHM);
        //加密
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            return byte2hex(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密算法
     *
     * @param password 加密密钥
     * @param data     被加密的数据
     */
    public static String decryptMode(String password, byte[] data) {
        try {
            //生成密钥
            SecretKey desKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ENCRYPT_ALGORITHM);
            //解密
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            return new String(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换成十六进制字符串
     */
    public static String byte2hex(byte[] b) {
        String tmp;
        StringBuilder builder = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                builder.append("0").append(tmp);
            } else {
                builder.append(tmp);
            }
            if (n < b.length - 1) {
                builder.append(":");
            }
        }
        return builder.toString()/*.toUpperCase()*/;
    }

    public static void main(String[] args) {
//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
       String encrypt = encryptMode("123456789012345678901234", "password".getBytes(StandardCharsets.UTF_8));
        System.out.println(encrypt);
        System.out.println(decryptMode("123456789012345678901234","8a:6e:b2:a9:ad:01:44:e7:93:35:cc:2f:c7:85:c2:6b".getBytes(StandardCharsets.UTF_8)));
    }
}