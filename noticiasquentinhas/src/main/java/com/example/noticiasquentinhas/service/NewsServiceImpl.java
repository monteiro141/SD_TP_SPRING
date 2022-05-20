package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        super();
        this.newsRepository = newsRepository;
    }

    @Override
    public News save(NewsForm newsForm, User user, Topics topics) {
        News news = new News(newsForm.getTitle(),newsForm.getContent(), newsForm.getCreationDate());
        news.setPublisher(user);
        news.setNews_topic_id(topics);
        return newsRepository.save(news);
    }

    @Override
    public Set<News> listNewsUser(String email) {
        return null;
    }

    @Override
    public Set<News> listNewsTopic(Timestamp timeStart, Timestamp timeEnd, String topic) {
        return null;
    }
}
