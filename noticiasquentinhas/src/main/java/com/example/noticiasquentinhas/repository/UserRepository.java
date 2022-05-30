package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
    public User findUsersByEmail(String email);
    @Query("SELECT u.email from User u join u.topics_subscriber s where s.topic_id = ?1 ")
    public ArrayList<String> findAllByTopics_subscriber(Integer topicID);
}


