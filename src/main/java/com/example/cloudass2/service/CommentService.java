package com.example.cloudass2.service;

import com.example.cloudass2.entity.Comment;

import java.util.List;

/**
 * @author miaos
 */
public interface CommentService {

    void addComment(Comment comment);

    List<Comment> findCommentsByArticleId(String articleId);
}
