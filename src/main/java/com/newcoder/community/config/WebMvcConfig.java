package com.newcoder.community.config;

import com.newcoder.community.controller.interceptor.AlphaInterceptor;
import com.newcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liuyang
 * @create 2023-02-10 10:24
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;


    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置需要拦截的请求的请求路径，?   匹配任何单字符
        //                            *	  匹配0或者任意数量的字符
        //                            **  匹配0或者更多的目录
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");


        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }
}
