package com.bin.controller;

import com.bin.bean.Message;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.MessageServiceImpl;
import com.bin.service.impl.UserService;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LetterController {
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @GetMapping("/letter")
    public String getLetter(Page page, Model model) {
        User user = hostHolder.getUser();
        Integer userId = user.getId();
        Integer rows = messageService.selectConversationsRows(userId);
        page.setPath("/letter");
        page.setRows(rows);
        List<Message> conversationList = messageService.selectConversations(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Message message : conversationList) {
            Integer anotherId = message.getFromId().equals(userId) ? message.getToId() : message.getFromId();
            User anotherUser = userService.selectUserById(anotherId);
            String conversationId = anotherId < userId ? anotherId + "_" + userId : userId + "_" + anotherId;
            Integer messageCount = messageService.selectMessagesRows(conversationId);
            Integer unreadMessageCount = messageService.selectMessagesUnreadRows(userId, conversationId);
            Map<String, Object> map = new HashMap<>();
            //放入每个会话发送者的用户信息
            map.put("fromUser", anotherUser);
            //会话的最新的那一条消息数据
            map.put("message", message);
            //会话中总共包括的消息数量
            map.put("messageCount", messageCount);
            //会话中未读的消息数量
            map.put("unreadMessageCount", unreadMessageCount);
            mapList.add(map);
        }
        model.addAttribute("messageList", mapList);
        return "site/letter";
    }

    @GetMapping("/letter-detail/{fromUserId}")
    public String getLetterDetail(@PathVariable("fromUserId") Integer fromUserId, Page page, Model model) {
        if (fromUserId == null)
            throw new IllegalArgumentException("参数错误！");
        User toUser = hostHolder.getUser();
        User fromUser = userService.selectUserById(fromUserId);
        Integer userId = toUser.getId();
        String conversationId = fromUserId < userId ? fromUserId + "_" + userId : userId + "_" + fromUserId;
        Integer messageCount = messageService.selectMessagesRows(conversationId);
        page.setRows(messageCount);
        page.setPath("/site/letter-detail");

        List<Message> messageList = messageService.selectMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messageMapList = new ArrayList<>();
        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();
            //把消息的内容放入
            map.put("message", message);
            //放入消息的发送者，由于是消息，所以有可能是登陆账号发送出去的
            map.put("fromUser", userService.selectUserById(message.getFromId()));
            messageMapList.add(map);
            if (message.getStatus() == 0)
                //将消息状态设置为已读
                messageService.updateOneMessageStatus(message.getId(), 1);
        }
        model.addAttribute("fromUser", fromUser);
        model.addAttribute("messageMapList", messageMapList);
        return "site/letter-detail";
    }

    @GetMapping("/delete-message/{messageId}")
    public String getDeleteMessage(@PathVariable("messageId") Integer messageId) {
        messageService.updateOneMessageStatus(messageId, 2);
        return "redirect:site/letter-detail";
    }

}
