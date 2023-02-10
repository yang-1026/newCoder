package com.newcoder.community.controller;

import com.alibaba.druid.support.opds.udf.ExportSelectListColumns;
import com.google.code.kaptcha.Producer;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.util.CommunityConstant;
import com.newcoder.community.util.CommunityUtil;
import com.newcoder.community.util.MailClient;
import com.sun.xml.internal.ws.developer.StreamingAttachment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.crypto.interfaces.PBEKey;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.PublicKey;
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



    @Autowired
    private Producer kaptchaProducer;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/kaptcha")
    //因为向浏览器输出的是验证码图片，不是字符串、html页面，所以使用void，自己使用response对象手动输出
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha",text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
           logger.error("响应验证码失败" + e.getMessage());
        }
    }






    @Value("${server.servlet.context-path}")
    private String contextPath;

    //处理登录信息
    @PostMapping("/login")
    //前面代码也有 /login 处理，但是get请求
    public String login(String username,String password,String code,boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response){

        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }

        //检查账号、密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            //如果是return视图名，是去访问html文件，而打开首页应该通过控制器方法去打开,所以要重定向
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }


    //处理退出业务
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }





    //忘记密码界面
    @GetMapping("/forget")
    public String getForgetPage(){
        return "/site/forget";
    }


    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    // 获取验证码
    @GetMapping("/forget/code")
    @ResponseBody
    public String getForgetCode(String email,HttpSession session){
        if(StringUtils.isBlank(email)){
            return CommunityUtil.getJSONString(1,"邮箱不能为空！");
        }

        // 校验邮箱是否注册，未注册则提示
        boolean exist = userService.isEmailExist(email);
        if(!exist){
            return CommunityUtil.getJSONString(1, "该邮箱尚未注册！");
        }


        // 发送邮件
        Context context = new Context();
        context.setVariable("email",email);
        String code = CommunityUtil.generateUUID().substring(0,4);
        context.setVariable("verifyCode", code);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMail(email,"找回密码",content);

        //session中保存验证码
        session.setAttribute("verifyCode", code);
        return CommunityUtil.getJSONString(0);
    }


    //重置密码
    @PostMapping("/forget/password")
    public String resetPassword(String email, String verifyCode, String password, Model model, HttpSession session){

        // 检查验证码
        String code = (String) session.getAttribute("verifyCode");
        if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(verifyCode)) {
            model.addAttribute("codeMsg", "验证码错误!");
            return "/site/forget";
        }

        Map<String, Object> map = userService.resetPassword(email, password);
        if(map.containsKey("user")) {
            return "redirect:/login";
        }else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/forget";
        }
    }




}
