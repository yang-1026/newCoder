package com.newcoder.community.util;

import com.newcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @create 2023-02-10 10:54
 */


/**
 * 持有用户信息,用于代替session对象.
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }

}
