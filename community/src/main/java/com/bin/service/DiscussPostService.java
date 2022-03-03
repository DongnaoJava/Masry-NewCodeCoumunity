package com.bin.service;

import com.bin.bean.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussPostService {
    List<DiscussPost> selectAllDiscussPosts(Integer offset, Integer limit);

    int selectAllDiscussPostRows();

    List<DiscussPost> selectDiscussPostsByUserId(Integer userId, Integer offset, Integer limit);

    int selectDiscussPostRowsByUserId(Integer userId);

    DiscussPost selectDiscussPostById(Integer id);

    int insertDiscussPost(DiscussPost discussPost);

    //更新帖子的评论数量
    int updateDiscussPostCommentCountById(Integer id,Integer commentCount);
}
