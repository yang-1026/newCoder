package com.newcoder.community;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.newcoder.community.entity.Message;
import com.newcoder.community.service.AlphaService;
import com.newcoder.community.service.MessageService;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.HtmlUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.LockSupport;


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


    @Test
    public void test(){
        String s = "1";
        System.out.println(s.charAt(0));
        Deque<Character> stack  = new ArrayDeque<>();
        stack.push('a');
        stack.push('b');
        stack.push('c');
//        String s1 = new String(String.valueOf(stack));
        String s1 = String.valueOf(stack);
        System.out.println(s1);

        StringBuilder builder = new StringBuilder();
        String s2 = new String(builder);
    }

}







