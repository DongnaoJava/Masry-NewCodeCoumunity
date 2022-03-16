package com.bin.controller;

import com.bin.annotation.LoginRequired;
import com.bin.bean.User;
import com.bin.service.impl.FollowServiceImpl;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired
    private FollowServiceImpl followService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @PostMapping("/follow")
    @ResponseBody
    public String follow(Integer entityType,Integer entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(),entityType,entityId);
        return CommunityUtil.getJSONString("0","已关注！");
    }

    @LoginRequired
    @PostMapping("/cancelFollow")
    @ResponseBody
    public String cancelFollow(Integer entityType,Integer entityId){
        User user = hostHolder.getUser();
        followService.cancelFollow(user.getId(),entityType,entityId);
        return CommunityUtil.getJSONString("1","取消关注！");
    }
}
