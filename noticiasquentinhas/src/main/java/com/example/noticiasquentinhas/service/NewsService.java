package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.forms.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public interface NewsService {
    public News save(NewsForm newsForm, User user, Topics topics);
    public News findNew(Integer id);
    public News saveEditNew(News news);
    public List<News>getNewsFromTimestamp(Topics topic, LocalDateTime date1, LocalDateTime date2, Integer pageNumber, Integer pageSize);
    public News getLastNewsFromTopic(Integer topicID);
    public List<News> getNews(Integer pageNumber, Integer pageSize);
    public List<News> getPublisherNews(String email,Integer pageNumber, Integer pageSize);
}
