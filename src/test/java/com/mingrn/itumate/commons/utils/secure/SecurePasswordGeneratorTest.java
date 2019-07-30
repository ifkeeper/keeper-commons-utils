package com.mingrn.itumate.commons.utils.secure;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SecurePasswordGeneratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurePasswordGeneratorTest.class);

    private static ExecutorService service = new ThreadPoolExecutor(4, Runtime.getRuntime().availableProcessors() * 2, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            service.execute(() -> LOGGER.info("{} ------------------- {}", SecurePasswordGenerator.INSTANCE.generate(), finalI));
        }
    }
}
