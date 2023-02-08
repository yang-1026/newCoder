package com.newcoder.community.config;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PublicKey;
import java.text.SimpleDateFormat;

/**
 * @author liuyang
 * @create 2023-02-07 11:38
 */


@Configuration
public class AlphaConfig {


    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
