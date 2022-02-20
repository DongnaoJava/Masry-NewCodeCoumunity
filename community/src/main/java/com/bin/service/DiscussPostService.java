package com.bin.service;

import com.bin.bean.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussPostService {
    List<DiscussPost> selectAllDiscussPosts(Integer offset,Integer limit);
    List<DiscussPost> selectDiscussPostsByUserId(Integer userId,Integer offset,Integer limit);
    int selectAllDiscussPostRows();
    int selectDiscussPostRowsByUserId(Integer userId);
}
