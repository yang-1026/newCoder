package com.newcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.newcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @create 2023-03-01 20:26
 */


@Component
public class EventProducer {

    @Autowired(required = false)
    private KafkaTemplate kafkaTemplate;

    //处理事件
    public void fireEvent(Event event){
        Gson gson = new Gson();
        //将事件发布到指定主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
