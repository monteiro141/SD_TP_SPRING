package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer news_id;
    private String title;
    private String content;
    private String creationDate;

    @ManyToOne
    @JoinColumn(name="news_topics", nullable = false)
    private Topics topics_news;


    @ManyToOne
    @JoinColumn(name="news_publisher", nullable = false)
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

    public Topics getTopics_news() {
        return topics_news;
    }

    public void setTopics_news(Topics topics_news) {
        this.topics_news = topics_news;
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
