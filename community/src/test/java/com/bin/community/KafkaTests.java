package com.bin.community;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bin.bean.User;
import com.bin.service.impl.UserServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SpringBootTest
public class KafkaTests {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testKafka(){
        User user = userService.selectUserById(159);
        kafkaProducer.sendMessage("test",user);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, User user) {
        kafkaTemplate.send(topic, String.valueOf(JSONObject.toJSON(user)));
    }
}

@Component
class KafkaConsumer {
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord<String, String> record) {
        JSONObject jsonObject = JSONObject.parseObject(record.value());
        User user = JSONObject.toJavaObject(jsonObject,User.class);
        System.out.println(user.getUsername());
        System.out.println(record);
    }
}