package com.example.cloudass2.service.impl;


import com.example.cloudass2.dao.ArticleDao;
import com.example.cloudass2.entity.Article;

import com.example.cloudass2.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("articleServiceImpl")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleDao articleDao;

    @Override
    public void addArticle(Article article) {
        articleDao.saveAndFlush(article);
    }

    @Override
    public void updateArticle(Article article) {
        articleDao.updateArticle(article.getId(),article.getContent(),article.getLastEditDate());
    }

    @Override
    public Article findArticleById(String id) {
        int intId = Integer.parseInt(id);
        Optional<Article> article = articleDao.findById(intId);
        if(article.isPresent())
        {
            return article.get();
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<Article> findAll() {
        return articleDao.findAllByOrderByLastEditDateDesc();
    }

    @Override
    public List<Article> findByType(String typeId) {
        return articleDao.findByTypeIdOrderByLastEditDateDesc(typeId);
    }

    @Override
    public Page<Article> findPage(int page,int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.Direction.DESC,"id");
        return articleDao.findAll(pageable);
    }



    @Override
    public int totalData() {
        return (int) articleDao.count();
    }
}
