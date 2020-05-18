package com.example.cloudass2.Service;

import com.example.cloudass2.Entity.Comment;

import java.util.List;

/**
 * @author miaos
 */
public interface CommentsService {

    void addComment(Comment comment);

    List<Comment> findCommentsByArticleId(String articleId);
}
