package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Topics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Topics;
    private String name;

    @OneToMany(mappedBy = "topics_news", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> news_topics;

    @ManyToOne
    @JoinColumn(name="topics_subscriber", nullable = true)
    private User subscriber;

    public Topics(){

    }

    public Integer getTopics() {
        return Topics;
    }

    public void setTopics(Integer topics) {
        Topics = topics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<News> getNews_topics() {
        return news_topics;
    }

    public void setNews_topics(Set<News> news_topics) {
        this.news_topics = news_topics;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }
}
