package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.Topics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Set;

public interface NewsRepository extends CrudRepository<News, Integer> {
    ArrayList<News> findAllByPublisher_Email(String email);
    ArrayList<News> findAll();
    @Query("select n from News n where n.topics_news.name = ?1")
    ArrayList<News> findAllByTopics_news(String topicName);
}
