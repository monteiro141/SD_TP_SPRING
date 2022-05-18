package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Publishers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer publisher_id;

    @OneToMany(mappedBy = "news_publisher_id", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> publisher_news_list;

    /*@OneToOne
    User user_publisher_full;

    public User getUser_publisher_full() {
        return user_publisher_full;
    }

    public void setUser_publisher_full(User user_publisher_full) {
        this.user_publisher_full = user_publisher_full;
    }*/

    public Publishers() {
    }

    public Integer getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(Integer publisher_id) {
        this.publisher_id = publisher_id;
    }

    public Set<News> getPublisher_news_list() {
        return publisher_news_list;
    }

    public void setPublisher_news_list(Set<News> publisher_news_list) {
        this.publisher_news_list = publisher_news_list;
    }


}
