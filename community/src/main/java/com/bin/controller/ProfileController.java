package com.bin.controller;

import com.bin.bean.CommunityConstant;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.FollowServiceImpl;
import com.bin.service.impl.LikeServiceImpl;
import com.bin.service.impl.UserServiceImpl;
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
    private UserServiceImpl userServiceImpl;
    @Autowired
    private LikeServiceImpl likeService;
    @Autowired
    private FollowServiceImpl followService;
    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable("userId") Integer userId, Model model) {
        //查看用户的id
        User user = userServiceImpl.selectUserById(userId);
        //登陆用户的id
        Integer loginUserId=-1;
        if (hostHolder.getUser() != null)
            loginUserId = hostHolder.getUser().getId();
        //查看的用户
        model.addAttribute("user", user);
        //登录用户
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

    @GetMapping("/followee/{targetUserId}")
    public String getFollowee(@PathVariable("targetUserId") Integer targetUserId, Model model, Page page) {
        //targetUserId是查看的目标用户
        Long followeeCount = followService.getFolloweeCount(targetUserId, ENTITY_TYPE_USER);
        page.setRows(followeeCount.intValue());
        page.setPath("/followee/" + targetUserId);

        List<Map<String, Object>> followee = followService.getFollowee(targetUserId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        //所有的关注对象
        model.addAttribute("mapList", followee);
        //查看的目标用户
        model.addAttribute("targetUser", userServiceImpl.selectUserById(targetUserId));

        //登陆用户的id
        Integer loginUserId = -1;
        if (hostHolder.getUser() != null)
            loginUserId = hostHolder.getUser().getId();

        model.addAttribute("loginUserId", loginUserId);
        return "/site/followee";
    }

    @GetMapping("/follower/{targetUserId}")
    public String getFollower(@PathVariable("targetUserId") Integer targetUserId, Model model, Page page) {
        //targetUserId是查看的目标用户
        Long followerCount = followService.getFollowerCount(ENTITY_TYPE_USER, targetUserId);
        page.setRows(followerCount.intValue());
        page.setPath("/follower/" + targetUserId);

        //登陆用户的id
        Integer loginUserId = -1;
        if (hostHolder.getUser() != null)
            loginUserId = hostHolder.getUser().getId();
        model.addAttribute("loginUserId", loginUserId);

        List<Map<String, Object>> follower = followService.getFollower(loginUserId, targetUserId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        model.addAttribute("mapList", follower);
        //查看的目标用户
        model.addAttribute("targetUser", userServiceImpl.selectUserById(targetUserId));

        return "/site/follower";
    }
}
