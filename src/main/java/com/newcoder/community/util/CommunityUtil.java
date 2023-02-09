package com.newcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author liuyang
 * @create 2023-02-08 21:17
 */

public class CommunityUtil {


    //生成随机字符串
    public static String generateUUID(){
        //UUID.randomUUID().toString()：获得字母、数字、横线的随机字符串
        return UUID.randomUUID().toString().replaceAll("-",""); //获得字母、数字的随机字符串
    }



    // MD5加密(不能解密)
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
