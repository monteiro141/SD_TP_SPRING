package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.entities.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface  UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}
