package com.newcoder.community;

import com.newcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyang
 * @create 2023-02-13 10:37
 */

@SpringBootTest
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1(){
        Object obj = alphaService.save1();
        System.out.println(obj);
    }

    @Test
    public void testSave2(){
        Object obj = alphaService.save2();
        System.out.println(obj);
    }





}
