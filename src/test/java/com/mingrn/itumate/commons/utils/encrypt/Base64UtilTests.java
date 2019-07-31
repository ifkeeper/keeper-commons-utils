package com.mingrn.itumate.commons.utils.encrypt;

import com.mingrn.itumate.commons.utils.secure.GeneratorIDFactory;
import com.mingrn.itumate.commons.utils.secure.SecurePasswordGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64UtilTests {

    // TOKEN 校验
    private final Pattern TOKEN_PATTERN = Pattern.compile("^([\\s\\S]{24})@(([\\w\\d]{2})(:[\\w\\d]{2})*)$", Pattern.MULTILINE);

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64UtilTests.class);

    @Test
    public void encryption() throws Exception {
        // 通过密码工具类生成 24 位密码
        String pwd = SecurePasswordGenerator.INSTANCE.generate(24);

        // 使用 ID 生成工具类生成一个 uId
        String uId = GeneratorIDFactory.generatorUserId();

        // 加密
        String encrypt = TripleDesUtil.encryptMode(pwd, uId.getBytes(StandardCharsets.UTF_8));

        LOGGER.info("\npwd: {}\nuId: {}\nencrypt: {}\n", pwd, uId, encrypt);

        // base64
        String base64 = Base64Util.encryption(pwd + "@" + encrypt, StandardCharsets.UTF_8);
        LOGGER.info("\nbase64: {}", base64);

        String desBase64 = Base64Util.decrypt(base64, StandardCharsets.UTF_8);
        LOGGER.info("\ndesBase64: {}", desBase64);

        Matcher matcher = TOKEN_PATTERN.matcher(desBase64);

        if (matcher.matches()){
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));

            System.out.println(TripleDesUtil.decryptMode(matcher.group(1), matcher.group(2)) + "\n");
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(Base64Util.md5(uId));
        }
    }
}
