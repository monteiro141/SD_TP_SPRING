package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Publishers {
    @Id
    private Integer publisher_id;
    private String name;
    private String email;
    private String password;
    private String role;
    @OneToMany(mappedBy = "news_publisher_id", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> publisher_news_list;

    public Integer getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(Integer publisher_id) {
        this.publisher_id = publisher_id;
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

    public Set<News> getPublisher_news_list() {
        return publisher_news_list;
    }

    public void setPublisher_news_list(Set<News> publisher_news_list) {
        this.publisher_news_list = publisher_news_list;
    }
}
