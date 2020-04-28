package com.example.cloudass2.service.impl;


import com.example.cloudass2.Dao.CommentDao;
import com.example.cloudass2.Entity.Comment;

import com.example.cloudass2.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService {

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
