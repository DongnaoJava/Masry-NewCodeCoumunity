package com.bin.controller;


import com.bin.bean.DiscussPost;
import com.bin.bean.User;
import com.bin.service.impl.DiscussPostServiceImpl;
import com.bin.service.impl.UserService;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@Controller
@RequestMapping("discuss")
public class DiscussPostController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private DiscussPostServiceImpl discussPostService;
    @Autowired
    private UserService userService;

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
        return CommunityUtil.getJSONString("0", "发送成功！");
    }

    @GetMapping("/detail/{id}")
    public String getDiscussPostDetail(@PathVariable("id") Integer id, Model model) {
        DiscussPost postDetail = discussPostService.selectDiscussPostById(id);
        User user = userService.selectUserById(postDetail.getUserId());
        model.addAttribute("postDetail",postDetail);
        model.addAttribute("user",user);
        return "site/discuss-detail";
    }
}
