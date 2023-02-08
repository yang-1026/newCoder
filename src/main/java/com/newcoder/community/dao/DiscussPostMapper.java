package com.newcoder.community.dao;

import com.newcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyang
 * @create 2023-02-07 21:42
 */


@Mapper
public interface DiscussPostMapper {

    //获取分页信息。当userId为0时，获取分页信息；不为0时，获取用户id为userId的分页信息（不算拉黑帖子信息）
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    //获取信息总条数（不算拉黑帖子信息）
    int selectDiscussPostRows(@Param("userId") int userIed);

}
