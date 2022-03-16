package com.bin.dao;

import com.bin.bean.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectAllCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit);

    int selectCountByEntity(Integer entityType, Integer entityId);

    /**
     * 插入一条评论或者回复
     * @param comment
     * @return
     */
    int insertComment(Comment comment);
    Comment selectCommentById(Integer id);
}
