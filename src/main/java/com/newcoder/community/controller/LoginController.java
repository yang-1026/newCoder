package com.newcoder.community.controller;

import com.alibaba.druid.support.opds.udf.ExportSelectListColumns;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.util.CommunityConstant;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author liuyang
 * @create 2023-02-08 21:06
 */


@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;


    //跳转到注册页面
    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    //跳转到登录界面
    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }


    //进行注册，获得注册数据,返回注册信息
    @PostMapping("/register")
    public String register(Model model,User user){
        Map<String, Object> map = userService.register(user);
        //注册成功,跳到operate-result页面
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        } else {
            //注册失败，跳到注册页面
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }


    //进行账号激活，返回激活信息
    // http://localhost:8080/community/activation/101/code
    @GetMapping("/activation/{id}/{activationCode}")
    public String activation(Model model, @PathVariable("id") int id,@PathVariable("activationCode") String activationCode){
        int result = userService.activation(id, activationCode);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作,该账号已经激活过了!");
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg","激活失败,您提供的激活码不正确!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }


}
