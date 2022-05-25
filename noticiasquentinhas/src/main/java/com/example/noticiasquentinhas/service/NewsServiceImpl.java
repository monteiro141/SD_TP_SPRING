package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
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
        news.setTopics_news(topics);
        return newsRepository.save(news);
    }

    @Override
    public ArrayList<News> listNewsUser(String email) {
        return newsRepository.findAllByPublisher_Email(email);
    }

    @Override
    public Set<News> listNewsTopic(Timestamp timeStart, Timestamp timeEnd, String topic) {
        return null;
    }

    @Override
    public ArrayList<News> listAllNews(){
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> findNew(Integer id){
        return newsRepository.findById(id);
    }



    @Override
    public News saveEditNew(News news){
        return newsRepository.save(news);
    }
}
