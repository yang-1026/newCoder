package com.newcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuyang
 * @create 2023-02-10 23:06
 */

//一个方法打上了这个注解表示只有在用户登录后才能访问。需要与拦截器相配合
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
