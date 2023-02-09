package com.newcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author liuyang
 * @create 2023-02-09 14:41
 */

@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100"); //宽度:100像素
        properties.setProperty("kaptcha.image.height", "40");   //高度：40像素
        properties.setProperty("kaptcha.textproducer.font.size", "32"); //字体大小
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0"); //字体颜色：黑色
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ"); //验证码随机字符范围
        properties.setProperty("kaptcha.textproducer.char.length", "4"); //验证码长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise"); //验证码干扰：例如拉伸、变形，但生成的验证码已经有干扰了，所以NoNoise

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
