package com.bin.controller;

import com.bin.bean.CommunityConstant;
import com.bin.bean.DiscussPost;
import com.bin.bean.Page;
import com.bin.bean.User;
import com.bin.service.impl.DiscussPostServiceImpl;
import com.bin.service.impl.LikeServiceImpl;
import com.bin.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostServiceImpl discussPostService;
    @Autowired
    private LikeServiceImpl likeService;

    @GetMapping({"/index", "/"})
    public String Index(Model model, Page page) {
        page.setRows(discussPostService.selectAllDiscussPostRows());
        page.setPath("/index");
        List<DiscussPost> discussPosts = discussPostService.selectAllDiscussPosts(page.getOffset(), page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.selectUserById(discussPost.getUserId());
            long likeCount = likeService.findLikeCount(ENTITY_TYPE_POST, discussPost.getId());
            //帖子
            map.put("post", discussPost);
            //帖子的作者
            map.put("user", user);
            //帖子的点赞数量
            map.put("likeCount",likeCount);

            mapList.add(map);
        }
        model.addAttribute("discussPosts",mapList);
        return "index";
    }

    @GetMapping("/error")
    public String getError(){
        return "error/500";
    }

}
