package com.example.cloudass2.Service.impl;


import com.example.cloudass2.Dao.CommentDao;
import com.example.cloudass2.Entity.Comment;

import com.example.cloudass2.Service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author miaos
 */
@Service("commentsServiceImpl")
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    CommentDao commentDao;

    @Override
    public void addComment(Comment comment) {
        commentDao.saveAndFlush(comment);
    }

    @Override
    public List<Comment> findCommentsByArticleId(String articleId) {
        return commentDao.findByArticleId(articleId);
    }
}
