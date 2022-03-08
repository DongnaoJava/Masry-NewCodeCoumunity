package com.bin.controller;


import com.bin.bean.*;
import com.bin.service.impl.CommentServiceImpl;
import com.bin.service.impl.DiscussPostServiceImpl;
import com.bin.service.impl.LikeServiceImpl;
import com.bin.service.impl.UserService;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private DiscussPostServiceImpl discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private LikeServiceImpl likeService;

    @ResponseBody
    @PostMapping("/add")
    public String addDiscussPost(String title, String content) {
        if (hostHolder.getUser() == null)
            return CommunityUtil.getJSONString("401", "您还没有登陆哦！");
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content))
            return CommunityUtil.getJSONString("403", "标题和内容都不能为空！");
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(hostHolder.getUser().getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.insertDiscussPost(discussPost);
        //有可能会报错，之后要统一处理
        return CommunityUtil.getJSONString("0", "发贴成功！");
    }

    @GetMapping("/detail/{id}")
    public String getDiscussPostDetail(@PathVariable("id") Integer id, Model model, Page page) {

        //帖子细节
        DiscussPost postDetail = discussPostService.selectDiscussPostById(id);
        model.addAttribute("postDetail", postDetail);

        //作者
        User user = userService.selectUserById(postDetail.getUserId());
        model.addAttribute("user", user);

        //点赞
        long postLikeCount = likeService.findLikeCount(ENTITY_TYPE_POST, id);
        model.addAttribute("postLikeCount", postLikeCount);
        if (hostHolder.getUser() == null)
            model.addAttribute("postIsLike", 0);
        else
            model.addAttribute("postIsLike", likeService.isLike(hostHolder.getUser().getId(), ENTITY_TYPE_POST, id));

        //评论分页信息
        page.setLimit(5);
        //在帖子表中存了评论数量，就不需要查询帖子数量了
        page.setRows(postDetail.getCommentCount());
        page.setPath("/discuss/detail/" + id);

        //评论：给帖子的评论
        //回复：给评论的评论
        List<Comment> commentList = commentService.selectAllCommentsByEntity(ENTITY_TYPE_POST, postDetail.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Comment comment : commentList) {
            //commentMap为每一个帖子显示对象
            Map<String, Object> commentMap = new HashMap<>();
            //把评论放入commentMap
            commentMap.put("comment", comment);
            //评论的作者放入commentMap
            User commentUser = userService.selectUserById(comment.getUserId());
            commentMap.put("commentUser", commentUser);
            //评论的点赞数量
            long commentLikeCount = likeService.findLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
            commentMap.put("commentLikeCount", commentLikeCount);
            if (hostHolder.getUser() == null)
                commentMap.put("commentIsLike", 0);
            else
                commentMap.put("commentIsLike", likeService.isLike(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId()));

            //把评论的回复放入commentMap
            List<Comment> commentReplyList = commentService.selectAllCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
            List<Map<String, Object>> mapList2 = new ArrayList<>();
            if (commentReplyList != null) {
                for (Comment commentReply : commentReplyList) {
                    //每一个回复的显示对象
                    Map<String, Object> replyMap = new HashMap<>();
                    //把回复放入replyMap
                    replyMap.put("reply", commentReply);
                    //把回复的作者放入replyMap
                    User replyUser = userService.selectUserById(commentReply.getUserId());
                    replyMap.put("replyUser", replyUser);
                    //把回复的目标放入replyMap
                    User targetUser = commentReply.getTargetId() == 0 ? null : userService.selectUserById(commentReply.getTargetId());
                    replyMap.put("targetUser", targetUser);
                    //回复的点赞数量
                    long replyLikeCount = likeService.findLikeCount(ENTITY_TYPE_COMMENT, commentReply.getId());
                    replyMap.put("replyLikeCount", replyLikeCount);
                    if (hostHolder.getUser() == null)
                        replyMap.put("replyIsLike", 0);
                    else
                        replyMap.put("replyIsLike", likeService.isLike(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, commentReply.getId()));

                    mapList2.add(replyMap);
                }
            }
            //把帖子的各种回复放入commentMap
            commentMap.put("replyList", mapList2);

            //把帖子的评论数量放入
            int commentCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
            commentMap.put("commentCount", commentCount);

            //把每个帖子放入到帖子列表
            mapList.add(commentMap);
        }
        model.addAttribute("commentMap", mapList);
        return "/site/discuss-detail";
    }
}