package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer news_id;
    private String title;
    private String content;
    private String creationDate;

    @ManyToOne
    @JoinColumn(name="topic_news_list", nullable = false)
    private Topics news_topic_id;


    @ManyToOne
    @JoinColumn(name="newsList", nullable = true)
    private User publisher;

    public News(){

    }

    public News(String title, String content, String creationDate) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Topics getNews_topic_id() {
        return news_topic_id;
    }

    public void setNews_topic_id(Topics news_topic_id) {
        this.news_topic_id = news_topic_id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }
}
