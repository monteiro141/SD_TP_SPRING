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
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private String role;
    private String profilePic;

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

    public User(String name, String lastName,String email, String password, boolean enabled, String role) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
        this.profilePic = null;
    }

    public User(){}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Transient
    public String getProfilePicPath(){
        if(profilePic == null || id == null)
            return "/profile-pics/0/defaultPic.png";
        return "/profile-pics/" + id + "/" + profilePic;
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
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", role='" + role + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", news_publisher=" + news_publisher +
                ", topics_subscriber=" + topics_subscriber +
                '}';
    }
}
