package com.newcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyang
 * @create 2023-03-01 20:17
 */


public class Event {

    //kafka主题
    private String topic;
    //事件触发者
    private int userId;
    //事件作用在什么实体类型上，如：帖子、评论等
    private int entityType;
    private int entityId;
    //实体作者
    private int entityUserId;
    //使事件具有扩展性,可以在该事件中保存一些额外信息
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    //下面都修改了set方法
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}
