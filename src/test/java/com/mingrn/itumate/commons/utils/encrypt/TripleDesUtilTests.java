package com.mingrn.itumate.commons.utils.encrypt;

import com.mingrn.itumate.commons.utils.secure.GeneratorIDFactory;
import com.mingrn.itumate.commons.utils.secure.SecurePasswordGenerator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class TripleDesUtilTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripleDesUtilTests.class);

    @Test
    public void encryptMode() {

        for (int i = 0; i < 100; i++) {
            System.out.println("用户：" + i);
            // 通过密码工具类生成 24 位密码
            String pwd = SecurePasswordGenerator.INSTANCE.generate(24);

            // 使用 ID 生成工具类生成一个 uId
            String uId = GeneratorIDFactory.generatorUserId();

            // 加密
            String encrypt = TripleDesUtil.encryptMode(pwd, uId.getBytes(StandardCharsets.UTF_8));

            LOGGER.info("\npwd: {}\nuId: {}\nencrypt: {}\n", pwd, uId, encrypt);
        }
    }
}