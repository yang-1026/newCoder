package com.newcoder.community.controller.interceptor;

import com.newcoder.community.annotation.LoginRequired;
import com.newcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author liuyang
 * @create 2023-02-10 23:09
 */

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    // 在Controller之前执行。即在controller方法执行之前判断带有@LoginRequired注解的方法是否能执行
    //Object handler：表示拦截的目标
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断拦截的目标是否是方法
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);


            //loginRequired != null：当前方法需要登录
            //hostHolder.getUser() == null：但并未登录
            if(loginRequired != null && hostHolder.getUser() == null){
                //因为没有登录，所以重定向到登录界面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
