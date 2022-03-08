package com.bin.service.impl;

import com.bin.bean.Comment;
import com.bin.bean.CommunityConstant;
import com.bin.dao.CommentMapper;
import com.bin.service.CommentService;
import com.bin.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService , CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostServiceImpl discussPostService;

    @Override
    public List<Comment> selectAllCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit) {
        return commentMapper.selectAllCommentsByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int selectCountByEntity(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {

        //添加评论
        if (comment==null)
            throw new IllegalArgumentException("参数不能为空！");
        String content =  sensitiveFilter.filter(comment.getContent());
        content = HtmlUtils.htmlEscape(content);
        comment.setContent(content);
        int rows = commentMapper.insertComment(comment);

        //更新评论数量
        Integer entityId = comment.getEntityId();
        Integer status = comment.getStatus();
        Integer entityType = comment.getEntityType();
        if(entityType==ENTITY_TYPE_POST&&status==0){
            int count = commentMapper.selectCountByEntity(entityType,entityId);
            discussPostService.updateDiscussPostCommentCountById(entityId,count);
        }
        return rows;
    }
}
