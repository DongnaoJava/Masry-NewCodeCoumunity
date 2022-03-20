package com.bin.event;

import com.alibaba.fastjson.JSONObject;
import com.bin.bean.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    //处理事件
    public void sendEvent(Event event){
        kafkaTemplate.send(event.getTopic(), String.valueOf(JSONObject.toJSON(event)));
    }
}
