package com.bin.dao;

import com.bin.bean.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(Integer userId,Integer offset,Integer limit);
    int selectDiscussPostRows(@Param("userId") Integer userId);
}
