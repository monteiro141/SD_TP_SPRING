package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;


public interface NewsService {
    public News save(NewsForm newsForm, User user, Topics topics);
    public Set<News> listNewsUser(String email);
    public Set<News> listNewsTopic(Timestamp timeStart,Timestamp timeEnd,String topic);
}
