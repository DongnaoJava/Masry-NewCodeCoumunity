package com.bin.community;

import com.bin.bean.Message;
import com.bin.service.impl.CommentServiceImpl;
import com.bin.service.impl.MessageServiceImpl;
import com.bin.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MessageTest {
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test(){
        List<Message> messageList = messageService.selectConversations(111,0,Integer.MAX_VALUE);
        for (Message message:messageList) {
            System.out.println(message);
        }
    }
    @Test
    public void test2(){
        System.out.println("/////////////////");
        List<Message> messageList = messageService.selectMessages("111_113",0,Integer.MAX_VALUE);
        for (Message message:messageList) {
            System.out.println(message);
        }
    }

    @Test
    public void test3(){
        Message message = new Message();

        message.setFromId(111);
        message.setToId(159);
        message.setConversationId("111_159");
        message.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape("我爱你")));
        message.setCreateTime(new Date());
        messageService.insertNewMessage(message);
    }
}
