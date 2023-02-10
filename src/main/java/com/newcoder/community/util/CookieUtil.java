package com.newcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author liuyang
 * @create 2023-02-10 10:43
 */

//从请求中获取指定cookie值
public class CookieUtil {

    public static String getValue(HttpServletRequest request,String cookieName){
        if(request == null || cookieName == null){
            throw new IllegalArgumentException("参数为空");
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
