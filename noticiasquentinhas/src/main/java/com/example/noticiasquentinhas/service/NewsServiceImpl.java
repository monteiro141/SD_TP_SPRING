package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.forms.NewsForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        super();
        this.newsRepository = newsRepository;
    }

    /**
     * Save a news to the repository
     * @param newsForm the news inserted by the publisher
     * @param user the user that write the news
     * @param topics the topic of the news
     * @return the news
     */
    @Override
    public News save(NewsForm newsForm, User user, Topics topics) {
        News news = new News(newsForm.getTitle(),newsForm.getContent(), newsForm.getCreationDate());
        news.setPublisher(user);
        news.setTopics_news(topics);
        return newsRepository.save(news);
    }

    /**
     * find a news with a specific id
     * @param id the news id
     * @return the news
     */
    @Override
    public News findNew(Integer id){
        return newsRepository.findById(id).get();
    }

    /**
     * Save a specific news
     * @param news the news
     * @return the news
     */
    @Override
    public News saveEditNew(News news){
        return newsRepository.save(news);
    }

    /**
     * Get all the news from a specific topic and a timestamp
     * @param topic the topic
     * @param date1 the first date
     * @param date2 the second date
     * @param pageNumber the page number
     * @param pageSize the pagesize
     * @return the news of a specic page (because of pagination)
     */
    @Override
    public List<News>getNewsFromTimestamp(Topics topic, LocalDateTime date1, LocalDateTime date2, Integer pageNumber, Integer pageSize){
        List<News> newsFromTimeStamp= new ArrayList<>();
        Pageable page = PageRequest.of(pageNumber,pageSize);
        List<News> newsFromTopic = newsRepository.findAllByTopics_news(topic.getName(), page);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (News news : newsFromTopic){
            if(date1.isBefore(LocalDateTime.parse(news.getCreationDate(),formatter)) && date2.isAfter(LocalDateTime.parse(news.getCreationDate(),formatter))){
                newsFromTimeStamp.add(news);
            }
        }
        return newsFromTimeStamp;
    }

    /**
     * Get the last news from a specific topic
     * @param topicID the topic id
     * @return the news
     */
    @Override
    public News getLastNewsFromTopic(Integer topicID){
        ArrayList<News> newsFromTopic= newsRepository.findAllByTopics_news(topicID);
        if(newsFromTopic.size()!=0)
            return newsFromTopic.get(newsFromTopic.size()-1);
        return null;
    }

    /**
     * Get all the news
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return the news list
     */
    @Override
    public List<News> getNews(Integer pageNumber, Integer pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        return newsRepository.findAll(page);
    }

    /**
     * Get the news of a specific user
     * @param email the user's email
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return the news list
     */
    @Override
    public List<News> getPublisherNews(String email,Integer pageNumber, Integer pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        return newsRepository.findAllByPublisher_Email(email,page);
    }

}
