package com.bin.controller;

import com.bin.bean.CommunityConstant;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.FollowServiceImpl;
import com.bin.service.impl.LikeServiceImpl;
import com.bin.service.impl.UserService;
import com.bin.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class ProfileController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private LikeServiceImpl likeService;
    @Autowired
    private FollowServiceImpl followService;
    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable("userId") Integer userId, Model model) {
        //查看用户的id
        User user = userService.selectUserById(userId);
        //登陆用户的id
        Integer loginUserId = hostHolder.getUser().getId();

        model.addAttribute("user", user);
        model.addAttribute("loginUserId", loginUserId);
        model.addAttribute("userGetLikeCount", likeService.findUserGetLikeCount(userId));
        //粉丝数量
        Long followerCount = followService.getFollowerCount(ENTITY_TYPE_USER, userId);
        //关注用户的数量
        Long followeeCount = followService.getFolloweeCount(userId, ENTITY_TYPE_USER);
        //是否关注该用户
        Integer follow = followService.getStatusOfFollow(loginUserId, ENTITY_TYPE_USER, userId);

        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followeeCount", followeeCount);
        model.addAttribute("follow", follow);
        return "/site/profile";
    }

    @GetMapping("/followee/{userId}")
    public String getFollowee(@PathVariable("userId") Integer userId, Model model,Page page) {
        Long followeeCount = followService.getFolloweeCount(userId, ENTITY_TYPE_USER);
        page.setRows(followeeCount.intValue());
        page.setPath("/followee/" + userId);

        List<Map<String, Object>> followee = followService.getFollowee(userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        model.addAttribute("mapList", followee);
        return "/site/followee";
    }
}
