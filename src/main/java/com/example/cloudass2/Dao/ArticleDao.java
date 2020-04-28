package com.example.cloudass2.Dao;

import com.example.cloudass2.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ArticleDao extends JpaRepository<Article, Integer> {

    Optional<Article> findById(int id);

    List<Article> findByTypeId(String typeId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Article a set a.content=?2,a.lastEditDate=?3 where a.id=?1")
    void updateArticle(int id, String content, Date lastEditDate);
}
