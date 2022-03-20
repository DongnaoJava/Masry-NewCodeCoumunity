package com.bin.controller;

import com.alibaba.fastjson.JSONObject;
import com.bin.bean.CommunityConstant;
import com.bin.bean.Message;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.NoticeServiceImpl;
import com.bin.service.impl.UserServiceImpl;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class NoticeController implements CommunityConstant {

    @Autowired
    private NoticeServiceImpl noticeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserServiceImpl userService;

    //系统通知页面
    @GetMapping("/notice/{userId}")
    public String getAllNoticesByUserId(@PathVariable("userId") Integer userId, Model model) {

        User user = hostHolder.getUser();
        if (user == null)
            return "redirect:index";
        //评论信息
        Date commentNewestNoticeCreateTime = null;
        User commentUser = null;
        String commentEntityType = null;
        Integer commentCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_COMMENT);
        Message commentNewestNotice = noticeService.selectOneNewestOneTypeNotice(userId, TOPIC_COMMENT);
        if (commentNewestNotice != null) {
            commentNewestNoticeCreateTime = commentNewestNotice.getCreateTime();
            String commentContent = commentNewestNotice.getContent();
            Map<String, Object> commentMap = JSONObject.parseObject(commentContent);
            //发起人id
            Integer commentUserId = (Integer) commentMap.get("userId");
            commentEntityType = (Integer) commentMap.get("entityType") == ENTITY_TYPE_POST ? "帖子" : "评论";
            commentUser = userService.selectUserById(commentUserId);
        }

        //点赞信息
        Date likeNewestNoticeCreateTime = null;
        User likeUser = null;
        String likeEntityType = null;
        Integer likeCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_LIKE);
        Message likeNewestNotice = noticeService.selectOneNewestOneTypeNotice(userId, TOPIC_LIKE);
        if (likeNewestNotice != null) {
            likeNewestNoticeCreateTime = likeNewestNotice.getCreateTime();
            String likeContent = likeNewestNotice.getContent();
            Map<String, Object> likeMap = JSONObject.parseObject(likeContent);
            //发起人id
            Integer likeUserId = (Integer) likeMap.get("userId");
            likeEntityType = (Integer) likeMap.get("entityType") == ENTITY_TYPE_POST ? "帖子" : "评论";
            likeUser = userService.selectUserById(likeUserId);
        }
        //关注信息
        Date followNewestNoticeCreateTime = null;
        User followUser = null;
        Integer followCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_FOLLOW);
        Message followNewestNotice = noticeService.selectOneNewestOneTypeNotice(userId, TOPIC_FOLLOW);
        if (followNewestNotice != null) {
            followNewestNoticeCreateTime = followNewestNotice.getCreateTime();
            String followContent = followNewestNotice.getContent();
            Map<String, Object> followMap = JSONObject.parseObject(followContent);
            //发起人id
            Integer followUserId = (Integer) followMap.get("userId");
            followUser = userService.selectUserById(followUserId);
        }

        Integer unreadCommentCount = noticeService.selectAllUnreadOneTypeNoticesCount(userId, TOPIC_COMMENT);
        Integer unreadLikeCount = noticeService.selectAllUnreadOneTypeNoticesCount(userId, TOPIC_LIKE);
        Integer unreadFollowCount = noticeService.selectAllUnreadOneTypeNoticesCount(userId, TOPIC_FOLLOW);
        model.addAttribute("commentNewestNoticeCreateTime", commentNewestNoticeCreateTime);
        model.addAttribute("commentEntityType", commentEntityType);
        model.addAttribute("likeNewestNoticeCreateTime", likeNewestNoticeCreateTime);
        model.addAttribute("likeEntityType", likeEntityType);
        model.addAttribute("followNewestNoticeCreateTime", followNewestNoticeCreateTime);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("followCount", followCount);
        model.addAttribute("unreadCommentCount", unreadCommentCount);
        model.addAttribute("unreadLikeCount", unreadLikeCount);
        model.addAttribute("unreadFollowCount", unreadFollowCount);
        model.addAttribute("commentUser", commentUser);
        model.addAttribute("likeUser", likeUser);
        model.addAttribute("followUser", followUser);
        model.addAttribute("loginUserId", user.getId());
        return "/site/notice";
    }

    //评论通知详情页面
    @GetMapping("/notice/detail/comment/{userId}")
    public String getCommentNoticeDetail(@PathVariable("userId") Integer userId, Page page, Model model) {
        User user = hostHolder.getUser();
        if (user == null)
            return "redirect:index";
        Integer allCommentNoticeCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_COMMENT);
        page.setPath("/notice/detail/comment/" + userId);
        page.setRows(allCommentNoticeCount);
        List<Message> commentMessageList = noticeService.selectAllOneTypeNotices(userId, TOPIC_COMMENT, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeMapList = new ArrayList<>();
        for (Message message : commentMessageList) {
            Map<String, Object> mapInfo = new HashMap<>();
            Date createTime = message.getCreateTime();
            //通知id
            mapInfo.put("messageId", message.getId());
            //创建时间
            mapInfo.put("createTime", createTime);
            //发起人id
            String content = message.getContent();
            Map<String, Object> map = JSONObject.parseObject(content);
            Integer initiatorId = (Integer) map.get("userId");
            User initiator = userService.selectUserById(initiatorId);
            //通知触发发起人
            mapInfo.put("initiator", initiator);
            //帖子的id
            Integer postId = (Integer) map.get("postId");
            //立即查看跳转页面路径
            mapInfo.put("directPath", "discuss/detail/" + postId);
            //评论的状态（未读，已读）
            mapInfo.put("status",message.getStatus());
            //评论对象的类型
            Integer entityType = (Integer) map.get("entityType");
            String entityTypeName = entityType == ENTITY_TYPE_POST ? "的帖子" : "的评论";
            mapInfo.put("entityTypeName", entityTypeName);
            noticeMapList.add(mapInfo);
        }
        model.addAttribute("noticeMapList", noticeMapList);
        model.addAttribute("loginUser", user);
        model.addAttribute("noticeType", "评论");

        //将未读消息状态更新为已读
        noticeService.updateOneTypeNoticeStatus(TOPIC_COMMENT, MESSAGE_STATE_READ);
        return "/site/notice-detail";
    }

    //点赞通知详情页面
    @GetMapping("/notice/detail/like/{userId}")
    public String getLikeNoticeDetail(@PathVariable("userId") Integer userId, Page page, Model model, Integer currentPage) {
        User user = hostHolder.getUser();
        if (user == null)
            return "redirect:index";
        Integer allLikeNoticeCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_LIKE);
        page.setPath("/notice/detail/like/" + userId);
        page.setRows(allLikeNoticeCount);
        List<Message> likeMessageList = noticeService.selectAllOneTypeNotices(userId, TOPIC_LIKE, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeMapList = new ArrayList<>();
        for (Message message : likeMessageList) {
            Map<String, Object> mapInfo = new HashMap<>();
            Date createTime = message.getCreateTime();
            //通知id
            mapInfo.put("messageId", message.getId());
            //创建时间
            mapInfo.put("createTime", createTime);
            //发起人id
            String content = message.getContent();
            Map<String, Object> map = JSONObject.parseObject(content);
            Integer initiatorId = (Integer) map.get("userId");
            User initiator = userService.selectUserById(initiatorId);
            //通知触发发起人
            mapInfo.put("initiator", initiator);
            //帖子的id
            Integer postId = (Integer) map.get("postId");
            //立即查看跳转页面路径
            mapInfo.put("directPath", "discuss/detail/" + postId);
            //评论的状态（未读，已读）
            mapInfo.put("status",message.getStatus());
            //点赞对象的类型
            Integer entityType = (Integer) map.get("entityType");
            String entityTypeName = entityType == ENTITY_TYPE_POST ? "的帖子" : "的评论";
            mapInfo.put("entityTypeName", entityTypeName);
            noticeMapList.add(mapInfo);
        }
        model.addAttribute("noticeMapList", noticeMapList);
        model.addAttribute("loginUser", user);
        model.addAttribute("noticeType", "点赞");

        //将未读消息状态更新为已读
        noticeService.updateOneTypeNoticeStatus(TOPIC_LIKE, MESSAGE_STATE_READ);
        return "/site/notice-detail";
    }

    //关注通知详情页面
    @GetMapping("/notice/detail/follow/{userId}")
    public String getFollowNoticeDetail(@PathVariable("userId") Integer userId, Page page, Model model, Integer currentPage) {
        User user = hostHolder.getUser();
        if (user == null)
            return "redirect:index";
        Integer allFollowNoticeCount = noticeService.selectAllOneTypeNoticesCount(userId, TOPIC_FOLLOW);
        page.setPath("/notice/detail/follow/" + userId);
        page.setRows(allFollowNoticeCount);
        List<Message> followMessageList = noticeService.selectAllOneTypeNotices(userId, TOPIC_FOLLOW, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeMapList = new ArrayList<>();
        for (Message message : followMessageList) {
            Map<String, Object> mapInfo = new HashMap<>();
            Date createTime = message.getCreateTime();
            //通知id
            mapInfo.put("messageId", message.getId());
            //创建时间
            mapInfo.put("createTime", createTime);
            //发起人id
            String content = message.getContent();
            Map<String, Object> map = JSONObject.parseObject(content);
            Integer initiatorId = (Integer) map.get("userId");
            User initiator = userService.selectUserById(initiatorId);
            //通知触发发起人
            mapInfo.put("initiator", initiator);
            //立即查看跳转页面路径
            mapInfo.put("directPath", "profile/" + initiator.getId());
            //评论的状态（未读，已读）
            mapInfo.put("status",message.getStatus());
            //关注对象的类型为空
            mapInfo.put("entityTypeName", null);
            noticeMapList.add(mapInfo);
        }
        model.addAttribute("noticeMapList", noticeMapList);
        model.addAttribute("loginUser", user);
        model.addAttribute("noticeType", "关注");

        //将未读消息状态更新为已读
        noticeService.updateOneTypeNoticeStatus(TOPIC_FOLLOW, MESSAGE_STATE_READ);
        return "/site/notice-detail";
    }
}
