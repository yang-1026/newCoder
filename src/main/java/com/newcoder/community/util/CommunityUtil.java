package com.newcoder.community.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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




    //将String字符串转化为json格式
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        Gson gson = new Gson();

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("code", code);
        map1.put("msg", msg);
        if(map != null){
            for(String key : map.keySet()){
                map1.put(key, map.get(key));
            }
        }
        return gson.toJson(map1);
    }


    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }


}
