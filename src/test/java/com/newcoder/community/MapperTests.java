package com.newcoder.community;

import com.newcoder.community.dao.DiscussPostMapper;
import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.entity.User;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author liuyang
 * @create 2023-02-07 19:46
 */


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;


    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }


    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }


    @Test
    public void updateUser(){
        int i = userMapper.updateStatus(150, 1);
        System.out.println(i);

        i = userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(i);

        i = userMapper.updatePassword(150, "hello");
        System.out.println(i);
    }


    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        list.forEach(System.out::println);

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }


}
