package com.example.cloudass2.service;

import com.example.cloudass2.entity.Article;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author miaos
 */
public interface ArticleService {
    void addArticle(Article article);

    void updateArticle(Article article);

    Article findArticleById(String id);

    List<Article> findAll();

    List<Article> findByType(String typeId);

    Page<Article> findPage(int page,int size);

    int totalData();
}
