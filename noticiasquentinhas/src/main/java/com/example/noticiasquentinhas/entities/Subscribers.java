package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subscribers {
    @Id
    private Integer subscriber_id;
    private String name;
    private String email;
    private String password;
    private String role;

    @ManyToMany
    private Set<Topics> subscribers_topics_list;


    public Integer getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(Integer subscriber_id) {
        this.subscriber_id = subscriber_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Topics> getSubscribers_topics_list() {
        return subscribers_topics_list;
    }

    public void setSubscribers_topics_list(Set<Topics> subscribers_topics_list) {
        this.subscribers_topics_list = subscribers_topics_list;
    }
}
