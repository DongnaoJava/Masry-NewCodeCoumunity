package com.bin.controller;

import com.bin.bean.Comment;
import com.bin.bean.CommunityConstant;
import com.bin.bean.User;
import com.bin.service.impl.CommentServiceImpl;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private HostHolder hostHolder;

    //增加一条对帖子的评论
    @ResponseBody
    @PostMapping("/addComment")
    public String addComment(int id, String content) {
        User user = hostHolder.getUser();
        if (user == null)
            return CommunityUtil.getJSONString("401", "您还没有登陆哦！");

        if (StringUtils.isBlank(content))
            return CommunityUtil.getJSONString("403", "标题和内容都不能为空！");
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setContent(content);
        comment.setCreateTime(new Date());
        comment.setEntityType(ENTITY_TYPE_POST);
        comment.setEntityId(id);
        commentService.insertComment(comment);
        return CommunityUtil.getJSONString("0", "评论成功！");
    }

    //增加一条对评论的回复
    @PostMapping("/addReply")
    public String addReply(int replyToEntityId1, String content, int postId, Integer replyToTargetId) {
        User user = hostHolder.getUser();
        if (user == null)
            throw new IllegalArgumentException("没有权限！");

        if (StringUtils.isBlank(content))
            throw new IllegalArgumentException("参数错误！");

        Comment comment = new Comment();
        if (replyToTargetId != null)
            comment.setTargetId(replyToTargetId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        comment.setCreateTime(new Date());
        comment.setEntityType(ENTITY_TYPE_COMMENT);
        comment.setEntityId(replyToEntityId1);
        commentService.insertComment(comment);
        return "redirect:/discuss/detail/" + postId;
    }
}
