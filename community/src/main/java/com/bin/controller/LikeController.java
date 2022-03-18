package com.bin.controller;

import com.bin.bean.User;
import com.bin.service.impl.LikeServiceImpl;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeServiceImpl likeService;
    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @PostMapping("/like")
    public String like(Integer entityId, Integer entityType,Integer discussPostId) {
        User user = hostHolder.getUser();
        if (user == null)
            return CommunityUtil.getJSONString("401", "您还没有登录！");
        likeService.like(user.getId(), entityType, entityId,discussPostId);
        Integer isLike = likeService.isLike(user.getId(), entityType, entityId);
        if (isLike == 1)
            return CommunityUtil.getJSONString("1", "点赞成功！");
        else
            return CommunityUtil.getJSONString("0", "取消点赞！");
    }
}
