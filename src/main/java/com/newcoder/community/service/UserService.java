package com.newcoder.community.service;

import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyang
 * @create 2023-02-07 22:07
 */


@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

}
