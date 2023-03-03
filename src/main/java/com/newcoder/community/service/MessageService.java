package com.newcoder.community.service;

import com.newcoder.community.dao.MessageMapper;
import com.newcoder.community.entity.Message;
import com.newcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author liuyang
 * @create 2023-02-14 21:50
 */


@Service
public class MessageService {

    @Autowired(required = false)
    private MessageMapper messageMapper;


    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }


    @Autowired
    private SensitiveFilter sensitiveFilter;

    //添加消息
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }


    //修改消息状态为已读
    public int updateMessageStatus(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

    //删除私信
    public int deleteMessage(int id){
        return messageMapper.updateStatus(Arrays.asList(new Integer[]{id}),2);
    }


    //查询某个主题下的最新通知
    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }


    // 查询某个主题所包含的通知数量
    public int findNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    // 查询未读的通知的数量
    public int findNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }


    // 查询某个主题所包含的通知列表
    public List<Message> findNotices(int userId,String topic,int offset,int limit){
       return messageMapper.selectNotices(userId,topic,offset,limit);
    }

}
