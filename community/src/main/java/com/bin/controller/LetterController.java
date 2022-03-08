package com.bin.controller;

import com.bin.bean.CommunityConstant;
import com.bin.bean.Message;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.MessageServiceImpl;
import com.bin.service.impl.UserService;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import com.bin.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
public class LetterController implements CommunityConstant {
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @GetMapping("/letter")
    public String getLetter(Page page, Model model) {
        User user = hostHolder.getUser();
        if(user==null)
            return "redirect:index";
        Integer userId = user.getId();
        Integer rows = messageService.selectConversationsRows(userId);
        page.setPath("/letter");
        page.setRows(rows);
        List<Message> conversationList = messageService.selectConversations(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Message message : conversationList) {
            Integer anotherId = message.getFromId().equals(userId) ? message.getToId() : message.getFromId();
            User anotherUser = userService.selectUserById(anotherId);
            String conversationId = getConversationId(anotherId,userId);
            Integer messageCount = messageService.selectMessagesRows(conversationId);
            Integer unreadMessageCount = messageService.selectMessagesUnreadRows(userId, conversationId);
            Map<String, Object> map = new HashMap<>();
            //放入每个会话对象的用户信息
            map.put("fromUser", anotherUser);
            //会话的最新的那一条消息数据
            map.put("message", message);
            //放入消息的发送者，由于是消息，所以有可能是登陆账号发送出去的
            map.put("messageSendUser",userService.selectUserById(message.getFromId()));
            //会话中总共包括的消息数量
            map.put("messageCount", messageCount);
            //会话中未读的消息数量
            map.put("unreadMessageCount", unreadMessageCount);
            mapList.add(map);
        }
        model.addAttribute("messageList", mapList);
        model.addAttribute("currentPage",page.getCurrent());
        return "site/letter";
    }
    @GetMapping("/letter-detail/{fromUserId}")
    public String getLetterDetail(@PathVariable("fromUserId") Integer fromUserId, Page page, Model model,Integer currentPage) {
        if (fromUserId == null)
            throw new IllegalArgumentException("参数错误！");
        User toUser = hostHolder.getUser();
        if(toUser==null)
            return "redirect:index";
        User fromUser = userService.selectUserById(fromUserId);
        Integer userId = toUser.getId();
        String conversationId = getConversationId(fromUserId,userId);
        Integer messageCount = messageService.selectMessagesRows(conversationId);
        page.setRows(messageCount);
        page.setPath("/letter-detail/"+fromUserId);

        List<Message> messageList = messageService.selectMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messageMapList = new ArrayList<>();
        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();
            //把消息的内容放入
            map.put("message", message);
            //放入消息的发送者，由于是消息，所以有可能是登陆账号发送出去的
            map.put("fromUser", userService.selectUserById(message.getFromId()));
            //账号登陆者
            map.put("userMe", toUser);
            messageMapList.add(map);
            if (message.getStatus() == MESSAGE_STATE_UNREAD && !userId.equals(message.getFromId()))
                //将消息状态设置为已读
                messageService.updateOneMessageStatus(message.getId(), MESSAGE_STATE_READ);
        }
        model.addAttribute("fromUser", fromUser);
        model.addAttribute("messageMapList", messageMapList);
        model.addAttribute("currentPage", currentPage);
        return "site/letter-detail";
    }

    @ResponseBody
    @PostMapping("/letter-send")
    public String sendLetter(String recipientName, String messageText) {
        //发送者
        User user = hostHolder.getUser();
        if (user == null)
            return CommunityUtil.getJSONString("401", "你还没有登录！");
        if (StringUtils.isBlank(recipientName) || StringUtils.isBlank(messageText))
            return CommunityUtil.getJSONString("403", "标题和内容都不能为空！");
        //接收者
        User recipient = userService.selectUserByName(recipientName);
        if (recipient == null)
            return CommunityUtil.getJSONString("403", "您要发送私信的用户不存在！");

        //插入一条新消息
        Message message = new Message();
        Integer userId = user.getId();
        Integer recipientId = recipient.getId();
        message.setFromId(userId);
        message.setToId(recipientId);
        message.setConversationId(getConversationId(recipientId,userId));
        message.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape(messageText)));
        message.setCreateTime(new Date());
        messageService.insertNewMessage(message);
        //有可能会报错，之后要统一处理

        return CommunityUtil.getJSONString("0","发送成功!");

    }

    @ResponseBody
    @PostMapping("/delete-message")
    public String getDeleteMessage(Integer messageId) {
        User user = hostHolder.getUser();
        if (user == null)
            return CommunityUtil.getJSONString("401", "你还没有登录！");
        messageService.updateOneMessageStatus(messageId, MESSAGE_STATE_DELETED);
        return CommunityUtil.getJSONString("0","删除成功!");
    }

    /**
     * 根据私信双方的id获取conversationId
     * @param fromUserId 登录用户接受私信或发送私信用户的id
     * @param userId 登陆的用户id
     * @return 返回conversationId
     */
    private String getConversationId(Integer fromUserId, Integer userId) {
        if (fromUserId == null || userId == null)
            throw new IllegalArgumentException("参数非法！");
        return fromUserId < userId ? fromUserId + "_" + userId : userId + "_" + fromUserId;
    }

}
