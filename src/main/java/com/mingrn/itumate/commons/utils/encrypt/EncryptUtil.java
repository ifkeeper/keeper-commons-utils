package com.mingrn.itumate.commons.utils.encrypt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密类,默认使用SHA-256加盐加密
 *
 * @author MinGRn
 */
public class EncryptUtil {
    private EncryptUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 对加密字符增加符号
     */
    private final static String SALT = "!@#";


    /**
     * 使用MD5加密且加盐
     */
    public static String encryptWithMD5(String strSrc) {
        return encrypt(strSrc + SALT, EncryptTypeEnum.MD5);

    }

    /**
     * 使用SHA-256加密且加盐
     */
    public static String encryptWithSHA256(String strSrc) {
        return encrypt(strSrc + SALT, EncryptTypeEnum.SHA256);

    }

    /**
     * 使用SHA-1加密
     */
    public static String encryptWithSHA1(String strSrc) {
        return encrypt(strSrc, EncryptTypeEnum.SHA1);
    }

    /**
     * 使用SHA-256加密,用户加密用户名密码
     * <p>
     * 密码加密规则:
     * <code>EncryptUtil.encryptWithSHA256(username.trim().toUpperCase().concat("@"+password))</code>
     * 示例:
     * <pre>{@code
     *   String username = "...";
     *   string password = "...";
     *
     *   EncryptUtil.encryptWithUserPassword(username, password);
     * }</pre>
     *
     * @param username 用户名
     * @param pwd      用户密码
     * @return 加密数据
     */
    public static String encryptWithUserPwd(String username, String pwd) {
        return encryptWithSHA256((username.trim().toUpperCase()) + "@" + (pwd.trim()));
    }

    /**
     * 密码校验
     *
     * @param username   用户名
     * @param pwd        用户密码
     * @param encryptPwd 加密的用户密码
     * @return 密码是否正确
     */
    public static boolean checkedPwd(String username, String pwd, String encryptPwd) {
        return encryptWithUserPwd(username, pwd).equals(encryptPwd);
    }

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc          要加密的字符串
     * @param encryptTypeEnum 加密类型  MD5,SHA-1,SHA-256
     */
    private static String encrypt(String strSrc, EncryptTypeEnum encryptTypeEnum) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(encryptTypeEnum.type);
            byte[] array = md.digest(strSrc.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /** 加密类型 */
    private enum EncryptTypeEnum {

        /** MD5 加密 */
        MD5("MD5"),

        /** SHA-1 加密 */
        SHA1("SHA-1"),

        /** SHA-256 加密 */
        SHA256("SHA-256");

        private String type;

        EncryptTypeEnum(String type) {
            this.type = type;
        }
    }
}
