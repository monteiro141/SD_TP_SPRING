package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.Topics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface NewsRepository extends CrudRepository<News, Integer> {
    ArrayList<News> findAllByPublisher_Email(String email);
    ArrayList<News> findAll();
    @Query("select n from News n ORDER BY n.news_id desc")
    List<News> findAll(Pageable P);
    @Query("select n from News n where n.topics_news.name = ?1 ORDER BY n.news_id desc")
    List<News> findAllByTopics_news(String topicName, Pageable P);
    @Query("select n from News n where n.publisher.email = ?1 ORDER BY n.news_id desc")
    List<News> findAllByPublisher_Email(String email, Pageable P);
    @Query("select n from News n where n.topics_news.topic_id = ?1")
    ArrayList<News>findAllByTopics_news(Integer topicId);
}
