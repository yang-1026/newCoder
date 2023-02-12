package com.newcoder.community.service;

import com.newcoder.community.dao.DiscussPostMapper;
import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author liuyang
 * @create 2023-02-07 22:03
 */


@Service
public class DiscussPostService {

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }


    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }



    @Autowired
    private SensitiveFilter sensitiveFilter;

    //处理新增帖子业务
    public int addDiscussPost(DiscussPost post){
        if(post == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        // 转义HTML标记
        //恶意注册的时候，会使用诸如 <script>alert('papapa')</script> 这样的名称，会导致网页打开就弹出一个对话框。
        //那么在转义之后，就没有这个问题了。
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

}
