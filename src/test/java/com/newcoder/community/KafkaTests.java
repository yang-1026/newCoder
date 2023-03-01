package com.newcoder.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @create 2023-02-28 22:55
 */

@SpringBootTest
public class KafkaTests {

    @Autowired(required = false)
    private KafkaProducer kafkaProducer;


    @Test
    public void testKafka() {
        kafkaProducer.sendMessage("test","你好");
        kafkaProducer.sendMessage("test","在吗");

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



//消费者
@Component
class KafkaProducer{

    @Autowired(required = false)
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }

}


//生产者
@Component
class KafkaConsumer {

    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord<String,String> record) {
        System.out.println(record.value());
    }
}