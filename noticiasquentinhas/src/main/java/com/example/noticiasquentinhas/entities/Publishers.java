package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Publishers extends User {
    @Id
    private Integer publisher_id;
    private String name;
    private String email;
    private String password;
    private String role;
    @OneToMany(mappedBy = "news_publiser_id", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> publisher_news_list;

    public Integer getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(Integer publisher_id) {
        this.publisher_id = publisher_id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    public Set<News> getPublisher_news_list() {
        return publisher_news_list;
    }

    public void setPublisher_news_list(Set<News> publisher_news_list) {
        this.publisher_news_list = publisher_news_list;
    }
}
