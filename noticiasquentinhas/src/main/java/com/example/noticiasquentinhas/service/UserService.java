package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.forms.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

public interface  UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
    User save(User user);
    User save(User user, String password);
    public String currentUserName(String email);
    public String currentUserRole(String email);
    public User search(String email);
    public ArrayList<String> getUsersWithSubscribedTopic(Integer topicID);
}
