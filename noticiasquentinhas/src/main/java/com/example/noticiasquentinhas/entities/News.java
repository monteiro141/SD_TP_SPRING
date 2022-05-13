package com.example.noticiasquentinhas.entities;

import javax.persistence.*;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer news_id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name="topic_news_list", nullable = false)
    private Topics news_topic_id;

    @ManyToOne
    @JoinColumn(name="publisher_news_list", nullable = false)
    private Publishers news_publisher_id;

    public News(){

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

    public Publishers getNews_user_id() {
        return news_publisher_id;
    }

    public void setNews_user_id(Publishers news_publisher_id) {
        this.news_publisher_id = news_publisher_id;
    }
}
