package com.example.cloudass2.service;

import com.example.cloudass2.Entity.Comment;

import java.util.List;

public interface CommentService {

    void addComment(Comment comment);

    List<Comment> findCommentsByArticleId(String articleId);
}
