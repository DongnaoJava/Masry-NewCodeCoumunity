package com.bin.service.impl;

import com.bin.bean.DiscussPost;
import com.bin.dao.DiscussPostMapper;
import com.bin.service.DiscussPostService;
import com.bin.util.CommunityUtil;
import com.bin.util.HostHolder;
import com.bin.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
/*
    这里在查询帖子的时候，要查出来所有的帖子，但是，这里查询出来的帖子只有用户的id，但是实际当中，我们需要展示的时用户的名字。所以有两种方法：
    1、user_id是user表的外键，所以可以用关联查询的方式
    2、单独的调用userMapper的service去查询相关的用户
    建议使用第二种方法，这种方法在之后用redis处理一些热点数据库的时候比较方便。代码也比较直观。
*/
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    //查询所有的帖子，分页查询
    @Override
    public List<DiscussPost> selectAllDiscussPosts(Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(0,offset,limit);
    }
    //根据用户id查询该用户所有的帖子
    @Override
    public List<DiscussPost> selectDiscussPostsByUserId(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }
    //查询所有用户帖子的总数量
    @Override
    public int selectAllDiscussPostRows() {
        return discussPostMapper.selectDiscussPostRows(0);
    }
    //根据id查询该用户的所有帖子数量
    @Override
    public int selectDiscussPostRowsByUserId(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public DiscussPost selectDiscussPostById(Integer id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    //插入一条帖子
    @Override
    public int insertDiscussPost(DiscussPost discussPost) {
        String title =discussPost.getTitle();
        String content = discussPost.getContent();
        //将用户输入的标题和内容进行敏感词过滤并转义
        discussPost.setTitle(sensitiveFilter.filter(HtmlUtils.htmlEscape(title)));
        discussPost.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape(content)));
        return discussPostMapper.insertDiscussPost(discussPost);
    }
}
