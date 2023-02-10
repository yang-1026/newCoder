package com.newcoder.community.service;

import com.newcoder.community.dao.LoginTicketMapper;
import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.entity.LoginTicket;
import com.newcoder.community.entity.User;
import com.newcoder.community.util.CommunityConstant;
import com.newcoder.community.util.CommunityUtil;
import com.newcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author liuyang
 * @create 2023-02-07 22:07
 */


@Service
public class UserService implements CommunityConstant {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }


    //处理注册业务
    public Map<String,Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(user == null){
            //IllegalArgumentException不合法的参数异常
            throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该账号已存在!");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该邮箱已被注册!");
            return map;
        }


        //为新用户设置初始状态
        user.setSalt(CommunityUtil.generateUUID().substring(0,5)); //5位随机字符串
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt())); // 用户输入密码+字符串进行MD5加密
        user.setType(0); //普通用户
        user.setStatus(0); //未激活
        user.setActivationCode(CommunityUtil.generateUUID()); //系统随机分配激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000))); //设置随机头像
        user.setCreateTime(new Date());
        userMapper.insertUser(user);


        //为用户发送激活邮件。模板html在 /mail/activation.html
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8080/community/activation/用户id/激活码
        String url= domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }



    //激活账号。激活时会得到用户id和激活码
    public int activation(int id,String activationCode){
        User user = userMapper.selectById(id);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(activationCode)){
            userMapper.updateStatus(id,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }



    @Autowired(required = false)
    private LoginTicketMapper loginTicketMapper;

    //处理登录业务
    public  Map<String,Object> login(String username,String password,long expiredSeconds){
        HashMap<String, Object> map = new HashMap<>();

        // 空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空!");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态。如果用户注册后未激活，也无法登录
        if(user.getStatus() == 0){
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码不正确!");
            return map;
        }


        //所有的验证通过，生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }



    //处理退出业务
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }



    // 判断邮箱是否已注册
    public boolean isEmailExist(String email) {
        User user = userMapper.selectByEmail(email);
        return user != null;
    }


    //重置密码
    public Map<String,Object> resetPassword(String email,String password){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(StringUtils.isBlank(email)){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证邮箱
        User user = userMapper.selectByEmail(email);
        if(user == null){
            map.put("emailMsg", "该邮箱尚未注册!");
            return map;
        }

        //重置密码
        password = CommunityUtil.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(),password);

        map.put("user", user);
        return map;
    }


    //查询用户凭证信息
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }


    //修改用户头像地址
    public int updateHeader(int userId,String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }


    // 修改密码
    public Map<String,Object> updatePassword(int userId,String oldPassword,String newPassword){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(StringUtils.isBlank(oldPassword)){
            map.put("oldPasswordMsg", "原密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "新密码不能为空!");
            return map;
        }

        // 验证原始密码
        User user = userMapper.selectById(userId);
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if(!user.getPassword().equals(oldPassword)){
            map.put("oldPasswordMsg", "原密码输入有误!");
            return map;
        }

        //更新密码
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(userId,newPassword);

        return map;
    }


}
