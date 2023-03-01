package com.newcoder.community;

import com.google.gson.Gson;
import com.newcoder.community.entity.Event;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import sun.net.idn.Punycode;

import java.io.*;
import java.util.ArrayList;

/**
 * @author liuyang
 * @create 2023-02-08 11:21
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTests {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void testLogger() {
        System.out.println(logger.getName());
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");

    }


}