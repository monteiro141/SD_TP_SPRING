package com.example.noticiasquentinhas.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private boolean enabled;
    private String role;

    @OneToMany(mappedBy = "publisher", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<News> news_publisher;

    @ManyToMany(mappedBy = "subscribers", cascade = CascadeType.PERSIST)
    private Set<Topics> topics_subscriber;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String name, String email, String password, boolean enabled, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }

    public User(){}

    /*OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Subscribers subscriber;

    @OneToOne(mappedBy = "user_publisher_full")
    public Publishers publisher_user_full;

    public Subscribers getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscribers subscriber) {
        this.subscriber = subscriber;
    }

    public Publishers getPublisher_user_full() {
        return publisher_user_full;
    }

    public void setPublisher_user_full(Publishers publisher_user_full) {
        this.publisher_user_full = publisher_user_full;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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



    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Set<News> getNews_publisher() {
        return news_publisher;
    }

    public void setNews_publisher(Set<News> news_publisher) {
        this.news_publisher = news_publisher;
    }

    public Set<Topics> getTopics_subscriber() {
        return topics_subscriber;
    }

    public void setTopics_subscriber(Set<Topics> topics_subscriber) {
        this.topics_subscriber = topics_subscriber;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                '}';
    }
}
