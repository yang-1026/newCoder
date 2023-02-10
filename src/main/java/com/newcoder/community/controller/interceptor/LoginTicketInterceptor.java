package com.newcoder.community.controller.interceptor;

import com.newcoder.community.entity.LoginTicket;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.util.CookieUtil;
import com.newcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author liuyang
 * @create 2023-02-10 10:41
 */


@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    // 在Controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        //ticket != null：说明从cookie中获取了登录凭证ticket，表明用户已经登录了
        if(ticket != null){
            // 查询用户凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效。过期时间应大于当前时间
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户信息，因为在postHandle中需要使用用户信息
                hostHolder.setUser(user);
            }
        }

        return true;
    }


    // 在Controller之后，TemplateEngine模板引擎之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    // 在TemplateEngine引擎之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
