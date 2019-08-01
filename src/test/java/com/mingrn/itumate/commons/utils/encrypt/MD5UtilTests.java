package com.mingrn.itumate.commons.utils.encrypt;

import com.mingrn.itumate.commons.utils.secure.SecurePasswordGenerator;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class MD5UtilTests {

    @Test
    public void md5() throws Exception {
        // 通过密码工具类生成 24 位密码
        String pwd = SecurePasswordGenerator.INSTANCE.generate(64);

        System.out.println(MD5Util.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8)));
    }
}
