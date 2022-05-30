package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface NewsService {
    public News save(NewsForm newsForm, User user, Topics topics);
    public ArrayList<News> listNewsUser(String email);
    public Set<News> listNewsTopic(Timestamp timeStart,Timestamp timeEnd,String topic);
    public ArrayList<News> listAllNews();
    public News findNew(Integer id);
    public News saveEditNew(News news);
    public ArrayList<News>getNewsFromTimestamp(Topics topic, LocalDateTime date1, LocalDateTime date2);
    public News getLastNewsFromTopic(Integer topicID);
    public List<News> getNews(Integer pageNumber, Integer pageSize);
    public List<News> getPublisherNews(String email,Integer pageNumber, Integer pageSize);
}
