package com.example.cloudass2.Dao;

import com.example.cloudass2.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentDao extends JpaRepository<Comment, Integer> {

    List<Comment> findByArticleId(String articleId);

}
