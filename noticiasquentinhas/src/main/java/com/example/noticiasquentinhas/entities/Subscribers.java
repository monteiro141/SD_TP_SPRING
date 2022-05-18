package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subscriber_id;

    /*@OneToOne
    @MapsId
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    @ManyToMany
    private Set<Topics> subscribers_topics_list;

    public Subscribers() {
    }

    public Integer getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(Integer subscriber_id) {
        this.subscriber_id = subscriber_id;
    }
    public Set<Topics> getSubscribers_topics_list() {
        return subscribers_topics_list;
    }

    public void setSubscribers_topics_list(Set<Topics> subscribers_topics_list) {
        this.subscribers_topics_list = subscribers_topics_list;
    }


}
