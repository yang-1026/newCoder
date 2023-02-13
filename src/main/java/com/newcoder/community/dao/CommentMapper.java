package com.newcoder.community.dao;

import com.newcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuyang
 * @create 2023-02-13 19:06
 */

@Mapper
public interface CommentMapper {

    //获取评论分页信息
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    //获取评论总数
    int selectCountByEntity(int entityType,int entityId);

    //添加评论
    int insertComment(Comment comment);

}
