package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Topics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Topics;
    private String name;

    @OneToMany(mappedBy = "news_topic_id", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> topic_news_list;

    @ManyToMany
    private Set<Subscribers> topics_subscribers_list;

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

    public Set<News> getTopic_news_list() {
        return topic_news_list;
    }

    public void setTopic_news_list(Set<News> topic_news_list) {
        this.topic_news_list = topic_news_list;
    }
}
