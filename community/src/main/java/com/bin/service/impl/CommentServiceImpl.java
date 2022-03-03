package com.bin.service.impl;

import com.bin.bean.Comment;
import com.bin.dao.CommentMapper;
import com.bin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectAllCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit) {
        return commentMapper.selectAllCommentsByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int selectCountByEntity(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    public int insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }

}
