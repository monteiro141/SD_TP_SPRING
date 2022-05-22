package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
//@SequenceGenerator(name="topic_id_gen", initialValue = 1, allocationSize = 100)
public class Topics {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_id_gen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topic_id;
    private String name;

    @OneToMany(mappedBy = "topics_news", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> news_topics;

    @ManyToMany
    @JoinColumn(name="topics_subscriber", nullable = true)
    private Set<User> subscribers;

    public Topics(){

    }

    public void addSubscriber(User user){
        subscribers.add(user);
    }

    public void removeSubscriber(User user){
        subscribers.remove(user);
    }

    public Integer getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id = topic_id;
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

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }
}
