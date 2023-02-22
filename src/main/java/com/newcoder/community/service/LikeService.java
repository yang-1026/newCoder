package com.newcoder.community.service;

import com.newcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author liuyang
 * @create 2023-02-22 21:19
 */


@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞或取消赞
    public void like(int usrId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, usrId);
        if(isMember){
            //已经点过赞了，取消点赞
            redisTemplate.opsForSet().remove(entityLikeKey,usrId);
        }else {
            //未点过赞，点赞
            redisTemplate.opsForSet().add(entityLikeKey,usrId);
        }
    }


    // 查询某实体点赞的数量
    public Long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态
    //返回1：已点赞；返回0：未点赞
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

}
