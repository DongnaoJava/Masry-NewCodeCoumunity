package com.bin.community;

import com.bin.bean.Message;
import com.bin.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MessageTest {
    @Autowired
    private MessageServiceImpl messageService;
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
        System.out.println(messageService.selectAllMessagesUnreadRows(159));
        System.out.println(messageService.selectMessagesUnreadRows(159,"111_159"));
    }
}
