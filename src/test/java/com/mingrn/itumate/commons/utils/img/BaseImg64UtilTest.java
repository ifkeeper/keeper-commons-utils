package com.mingrn.itumate.commons.utils.img;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class BaseImg64UtilTest {

    @Test
    public void testGetBaseImg64StrFromDisk() {
        long start = System.currentTimeMillis();
        String base64 = BaseImg64Util.getBaseImg64StrFromDisk("C:\\Users\\MinGRn\\Downloads\\Pastel Tides.jpg");
        System.out.println(base64);
        System.out.println("\n用时: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testGetBaseImg64StrFromNet() throws IOException {
        long total = 0;
        List<String> list = new LinkedList<>();
        for (int i = 0; i < 2000; i++) {
            long start = System.currentTimeMillis();
//            String base64 = BaseImg64Util.getBaseImg64StrFromNet("https://test-one-road.oss-cn-hangzhou.aliyuncs.com/111.jpg");
            String base64 = BaseImg64Util.getBaseImg64StrFromNetWithHttpClient("https://test-one-road.oss-cn-hangzhou.aliyuncs.com/111.jpg");
            System.out.println(base64);
//            System.out.println("\n用时: " + (System.currentTimeMillis() - start));
            long use = (System.currentTimeMillis() - start);
            total += use;
            list.add("用时: " + use);
        }
        list.forEach(System.out::println);
        System.out.println("平均用时: " + (total / list.size()));
    }
}
