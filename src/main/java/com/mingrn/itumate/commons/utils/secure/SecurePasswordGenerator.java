package com.mingrn.itumate.commons.utils.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 强安全密码生成类
 * <p>
 * 使用示例:
 * <pre>{@code
 *   // 生成默认级别密码
 *   // 密码默认长度为8,包含的字符:字母(区分大小写)、数值和特殊字符
 *   String pwd = SecurePasswordGenerator.INSTANCE.generate();
 *
 *   // 指定长度密码
 *   // 密码的最小长度为8,最大长度为64
 *   int pwdLen = 10;
 *   String pwd = SecurePasswordGenerator.INSTANCE.generate(pwdLen);
 *
 *   // 指定密码长度与字符类型
 *   // 字符类型包括26英文字母(区分大消息)、数值、特殊字符
 *   // 当指定密码字符类型为1时,会进行排除数值与特殊字符
 *   // 这里指定的类型为 3,则生成的密码最多包括的字符类型有上面四种的其中三种
 *   int pwdLen = 10, characterVarious = 3;
 *   String pwd = SecurePasswordGenerator.INSTANCE.generate(pwdLen);
 *  }</pre>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/7/2 18:27
 */
public enum SecurePasswordGenerator {

    /** 实例化 */
    INSTANCE;

    /**
     * 密码默认包含的特殊字符
     */
    private static final char[] DEFAULT_SPECIAL_CHARACTERS = {
            '~', '!', '@', '#', '$',
            '%', '^', '&', '*', '(',
            ')', '-', '_', '=', '+',
            '[', '{', '}', ']', '|',
            ';', ':', ',', '<', '.',
            '>', '?', '\\', '/', '`'
    };

    /**
     * 26 英文字母
     */
    private static final int LETTER_RANGE = 26;

    /**
     * 随机数范围
     */
    private static final int NUMBER_RANGE = 10;

    /**
     * 默认密码长度
     */
    private static final int DEFAULT_PASSWORD_LEN = 8;

    /**
     * 最大密码长度
     */
    private static final int MAX_PASSWORD_LEN = 64;

    /**
     * 最大字符种类
     */
    private static final int MAX_CHARACTER_VARIOUS = 4;

    /**
     * 特殊字符范围
     */
    private static final int SPECIAL_CHARACTERS_RANGE = DEFAULT_SPECIAL_CHARACTERS.length;

    /**
     * 强随机数
     */
    private static SecureRandom secure;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurePasswordGenerator.class);

    public String generate() {
        return generate(DEFAULT_PASSWORD_LEN);
    }

    /**
     * @param passwordLen 密码长度
     */
    public String generate(int passwordLen) {
        return generate(passwordLen, MAX_CHARACTER_VARIOUS);
    }

    /**
     * @param passwordLen      密码的长度
     * @param characterVarious 密码包含字符的种类
     */
    public String generate(int passwordLen, int characterVarious) {

        if (characterVarious <= 0) {
            throw new IllegalArgumentException("the password character Various is negative or zero");
        }

        if (passwordLen <= 0) {
            throw new IllegalArgumentException("the password length is negative or zero");
        }

        if (passwordLen > MAX_PASSWORD_LEN) {
            passwordLen = MAX_PASSWORD_LEN;
        }

        if (characterVarious > MAX_CHARACTER_VARIOUS) {
            characterVarious = MAX_CHARACTER_VARIOUS;
        }

        // 禁止手动设置种子,应该使用系统默认随机源
        try {
            secure = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SecureRandom Can't Generate SHA1PRNG Instance: {}, Will call not initialized parameter structure", e.getMessage());
            secure = new SecureRandom();
        }

        return generateSecurePassword(passwordLen, characterVarious);
    }

    /**
     * 安全密码生成
     *
     * @return 密码字符串
     */
    private String generateSecurePassword(int passwordLen, int characterVarious) {
        char[] password = new char[passwordLen];
        List<Integer> pwCharsIndex = new ArrayList<>();
        for (int i = 0; i < password.length; i++) {
            pwCharsIndex.add(i);
        }

        // 获取所有密码类型
        // 当要求密码类型为 1 时排除特殊字符与数值
        List<CharacterType> allTypes = new ArrayList<>(Arrays.asList(CharacterType.values()));
        if (characterVarious == 1) {
            allTypes.remove(CharacterType.NUMBER);
            allTypes.remove(CharacterType.SPECIAL_CHARACTER);
        }

        // 随机获取 characterVarious 种密码类型
        List<CharacterType> takeTypes = new ArrayList<>();
        for (int i = 0; i < characterVarious; i++) {
            takeTypes.add(allTypes.remove(secure.nextInt(allTypes.size())));
        }

        //随机填充一位密码
        while (pwCharsIndex.size() > 0) {
            int pwIndex = pwCharsIndex.remove(secure.nextInt(pwCharsIndex.size()));
            Character character = generateSeed(takeTypes.get(secure.nextInt(takeTypes.size())));

            password[pwIndex] = character;
        }
        return String.valueOf(password);
    }

    /**
     * 随机密码 seed 生成
     *
     * @param type 要求字符类型
     */
    private Character generateSeed(CharacterType type) {
        int seed;
        char c;
        switch (type) {
            case LOWERCASE:
                // 随机小写字母
                seed = secure.nextInt(LETTER_RANGE);
                seed += 97;
                c = (char) seed;
                break;
            case UPPERCASE:
                //随机大写字母
                seed = secure.nextInt(LETTER_RANGE);
                seed += 65;
                c = (char) seed;
                break;
            case NUMBER:
                //随机数字
                seed = secure.nextInt(NUMBER_RANGE);
                seed += 48;
                c = (char) seed;
                break;
            default:
                // 特殊字符 SPECIAL_CHARACTER
                seed = secure.nextInt(SPECIAL_CHARACTERS_RANGE);
                c = DEFAULT_SPECIAL_CHARACTERS[seed];
                break;
        }
        return c;
    }

    /**
     * 密码允许字符类型
     */
    private enum CharacterType {
        /**
         * 数值
         */
        NUMBER,
        /**
         * 小写字母
         */
        LOWERCASE,
        /**
         * 大写字母
         */
        UPPERCASE,
        /**
         * 特殊字符
         */
        SPECIAL_CHARACTER
    }
}