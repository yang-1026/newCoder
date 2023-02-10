package com.newcoder.community.controller;

import com.newcoder.community.entity.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.util.CommunityUtil;
import com.newcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author liuyang
 * @create 2023-02-10 19:33
 */

@Controller
@RequestMapping("/user")
public class UserController {

    //访问设置页面
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    //拦截器起作用后，存有用户信息
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;


    //上传文件
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","您还未选择图片");
            return "/site/setting";
        }

        //获取用户传入的文件名
        String filename = headerImage.getOriginalFilename();
        //获取文件的后缀 .xxx
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + filename);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.debug("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!",e);
        }


        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png （/user/header自己定义的)
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        User user = hostHolder.getUser();
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }



    //读取上传的头像
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        // 服务器存放图片的路径
        fileName = uploadPath + "/" + fileName;
        // 获取文件后缀：例如xxx.jpg ,获得jpg
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //设置输出文件格式
        response.setContentType("image/" + suffix);
        //以二进制流的形式输出图片
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            //response输出流由springboot管理，自动会关闭
            ServletOutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //修改密码
    @PostMapping("updatePassword")
    public String updatePassword(String oldPassword,String newPassword,Model model){
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if(map == null || map.isEmpty()){
            return "redirect:/logout";
        }else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }



}
