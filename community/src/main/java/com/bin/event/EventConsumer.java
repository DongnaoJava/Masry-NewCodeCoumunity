package com.bin.event;

import com.alibaba.fastjson.JSONObject;
import com.bin.bean.CommunityConstant;
import com.bin.bean.Event;
import com.bin.bean.Message;
import com.bin.service.impl.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EventConsumer implements CommunityConstant {
    @Autowired
    private MessageServiceImpl messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleEvent(ConsumerRecord<String, String> record) {
        String stringOfEvent = record.value();
        if (stringOfEvent == null) {
            log.error("消息的内容为空！");
            return;
        }
        Event event = JSONObject.parseObject(stringOfEvent).toJavaObject(Event.class);
        if (event == null) {
            log.error("消息格式错误！");
            return;
        }
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getUserId());
        message.setCreateTime(event.getCreateTime());
        message.setConversationId(record.topic());
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("userId", event.getEntityUserId());
        contentMap.put("entityType", event.getEntityType());
        contentMap.put("entityId", event.getEntityId());
        //查看多余的信息是否为空，如果不是，将他放入内容中
        if (!event.getData().isEmpty())
            event.getData().forEach(contentMap::put);
        message.setContent(String.valueOf(JSONObject.toJSON(contentMap)));
        messageService.insertNewMessage(message);
    }
}
