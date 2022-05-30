package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    public News findNew(Integer id){
        return newsRepository.findById(id).get();
    }

    @Override
    public News saveEditNew(News news){
        return newsRepository.save(news);
    }

    @Override
    public ArrayList<News>getNewsFromTimestamp(Topics topic, LocalDateTime date1, LocalDateTime date2){
        ArrayList<News> newsFromTimeStamp= new ArrayList<>();
        ArrayList<News> newsFromTopic= newsRepository.findAllByTopics_news(topic.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (News news : newsFromTopic){
            if(date1.isBefore(LocalDateTime.parse(news.getCreationDate(),formatter)) && date2.isAfter(LocalDateTime.parse(news.getCreationDate(),formatter))){
                newsFromTimeStamp.add(news);
            }
        }
        return newsFromTimeStamp;
    }

    @Override
    public News getLastNewsFromTopic(Integer topicID){
        ArrayList<News> newsFromTopic= newsRepository.findAllByTopics_news(topicID);
        if(newsFromTopic.size()!=0)
            return newsFromTopic.get(newsFromTopic.size()-1);
        return null;
    }

    @Override
    public List<News> getNews(Integer pageNumber, Integer pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        return newsRepository.findAll(page);
    }

    @Override
    public List<News> getPublisherNews(String email,Integer pageNumber, Integer pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        return newsRepository.findAllByPublisher_Email(email,page);
    }

}
